package org.bff.files.formatted.reader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.BatchedLineIterator;
import org.bff.files.formatted.processors.ReadPostprocessor;
import org.bff.files.formatted.processors.ReadPreprocessor;
import org.bff.files.utils.FFUtils;
import org.bff.files.utils.Utils;
import org.bff.formatted.model.annotations.FormattedField;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a base for classes that read text lines from a given <code>Reader</code>, and
 * transforms them into the given data type.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public abstract class FormattedReader <Type>
{
	///region FIELDS

	/**
	 * Internal reader. Used to read data from the given <code>Reader</code>.
	 */
	private final BatchedLineIterator lineIterator;

	/**
	 * Class used as base for processing.
	 */
	protected final Class <Type> klass;

	/**
	 * List of <code>klass</code> fields annotated as <code>@FormattedField</code>, sorted
	 * by position, in increasing order.
	 */
	protected final List <Field> formattedFields;

	protected final ReadPreprocessor preprocessor;

	protected final ReadPostprocessor postprocessor;

	///endregion
	
	///region CONSTRUCTORS

	/**
	 * For use of derived classes to initialize common fields.
	 */
	protected FormattedReader (
		final Class <Type> klass,
		final BatchedLineIterator lineIterator,
		final ReadPreprocessor preprocessor,
		final ReadPostprocessor postprocessor)
	{
		if (Utils.isAnyNull (klass, lineIterator, preprocessor, postprocessor))
		{
			throw new IllegalArgumentException ("Arguments cannot be null");
		}

		this.formattedFields = FFUtils.getFormattedFields (klass);
		this.klass = klass;
		this.lineIterator = lineIterator;
		this.preprocessor = preprocessor;
		this.postprocessor = postprocessor;
	}

	///endregion
	
	///region PUBLIC METHODS: API
	
	/**
	 * Force-closes the reader.
	 **/
	public void close () throws IOException
	{
		lineIterator.close ();
	}
	
	/**
	 * <code>true</code> if there are more lines to read.
	 **/
	public boolean hasNext ()
	{
		return lineIterator.hasNext ();
	}
	
	/**
	 * Reads the following batch of lines, and returns them as a <code>Stream</code>.
	 **/
	public Stream <Type> read ()
	{
		return lineIterator.read ().map (this::mapLine);
	}

	/**
	 * Reads the following batch of lines, and returns them as a <code>List</code>.
	 **/
	public List <Type> readAsList ()
	{
		return read ().collect (Collectors.toList ());
	}
	
	/**
	 * Skips the next <code>skipCount</code> number of lines.
	 **/
	public void skip (int skipCount)
	{
		lineIterator.skip (skipCount);
	}

	///endregion
	
	///region PROTECTED METHODS

	/**
	 * Processes the given <code>line</code>, and converts it into an appropriate object.
	 */
	private Type mapLine (final String line)
	{
		try
		{
			final Type classInstance = ConstructorUtils.invokeConstructor (klass);
			final List <String> tokens = tokenize (line);

			for (Field field : formattedFields)
			{
				final FormattedField formattedField = FFUtils.getFormattedField (field);
				final int position = formattedField.position ();

				if (Utils.within (position - 1, 0, tokens.size ()))
				{
					final String token = tokens.get (position - 1);
					Object result = null;

					String cleaned = clean (field, preprocessor.process (token, position));

					if (!(formattedField.optional () && StringUtils.isBlank (cleaned)))
					{
						Object processed = Utils.cast (field, cleaned);

						result = postprocessor.process (processed, position);
					}

					FieldUtils.writeField (field, classInstance, result, true);
				}
			}

			return classInstance;
		}

		catch (Exception e)
		{
			e.printStackTrace ();

			return null;
		}
	}

	protected String clean (Field field, String fieldValue)
	{
		return fieldValue;
	}

	protected abstract List <String> tokenize (final String line);

	///endregion
}
