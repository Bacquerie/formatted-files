package org.bff.files.formatted.reader;

import org.bff.files.BatchedLineIterator;
import org.bff.files.formatted.processors.ReadPostprocessor;
import org.bff.files.formatted.processors.ReadPreprocessor;
import org.bff.files.utils.FFUtils;
import org.bff.files.utils.Utils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FormattedReader <Type>
{
	///region FIELDS

	/**
	 * Internal reader. Used to read data from the given <code>Reader</code>.
	 */
	private final BatchedLineIterator lineIterator;

	protected final Class <Type> klass;
	protected final List <Field> formattedFields;
	protected final ReadPreprocessor preprocessor;
	protected final ReadPostprocessor postprocessor;

	///endregion
	
	///region CONSTRUCTORS

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

		this.formattedFields = FFUtils.findFormattedFields (klass);
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
	protected abstract Type mapLine (final String line);

	///endregion
}
