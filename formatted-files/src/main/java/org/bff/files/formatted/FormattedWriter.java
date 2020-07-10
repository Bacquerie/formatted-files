package org.bff.files.formatted;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.formatted.processors.WritePostprocessor;
import org.bff.files.formatted.processors.WritePreprocessor;
import org.bff.files.utils.FFUtils;
import org.bff.files.utils.Utils;
import org.bff.formatted.model.annotations.FormattedField;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Provides a base for classes that write objects into writers as formatted text lines.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public abstract class FormattedWriter <Type>
{
	///region FIELDS

	/**
	 * Line separator to use when writing.
	 */
	protected static final String lineSeparator = System.lineSeparator ();

	/**
	 * Default file encoding.
	 */
	protected static final Charset encoding = Charset.defaultCharset ();

	/**
	 * Data type containing the <code>@FormattedField</code>s to read.
	 */
	protected final Class <Type> klass;

	/**
	 * List of fields from <code>klass</code> annotated as <code>@FormattedField</code>,
	 * sorted by <code>position</code>.
	 */
	protected final List <Field> formattedFields;

	/**
	 * Allows for data transformations before converting the field values to string.
	 */
	protected final WritePreprocessor preprocessor;

	/**
	 * Allows for data transformations after the field values have been converted to their
	 * target types.
	 */
	protected final WritePostprocessor postprocessor;

	///endregion

	///region CONSTRUCTORS

	/**
	 * Intended to be used by derived classes, to initialize internal attributes.
	 */
	protected FormattedWriter (
		final Class <Type> klass,
		final WritePreprocessor preprocessor,
		final WritePostprocessor postprocessor)
	{
		if (Utils.isAnyNull (klass, preprocessor, postprocessor))
		{
			throw new IllegalArgumentException ("No argument can be null");
		}

		this.formattedFields = FFUtils.getFormattedFields (klass);

		this.klass = klass;
		this.preprocessor = preprocessor;
		this.postprocessor = postprocessor;
	}

	///endregion

	///region PUBLIC METHODS: WRITERS

	/**
	 * Writes the contents of <code>stream</code> into the given <code>writer</code>. Each item in
	 * the stream will represent a text line in the output.
	 */
	public Writer write (final Stream <Type> stream, final Writer writer) throws IOException
	{
		if (Utils.isAnyNull (stream, writer))
		{
			throw new IllegalArgumentException ("Arguments cannot be null");
		}

		final String [] lines = stream.map (this::mapItem).toArray (String[]::new);

		writer.append (String.join (lineSeparator, lines)).flush ();

		return writer;
	}

	/**
	 * Writes the contents of <code>stream</code> into the given <code>file</code>, which is
	 * expected to be encoded by <code>encoding</code>. Each item in the stream will represent
	 * a text line in the file.
	 */
	public void writeToFile (final Stream <Type> stream, final File file, final Charset encoding)
		throws IOException
	{
		write (stream, new OutputStreamWriter (new FileOutputStream (file), encoding));
	}

	/**
	 * Writes the contents of <code>stream</code> into the given <code>file</code>, which is
	 * expected to be encoded by the default charset. Each item in the stream will represent
	 * a text line in the file.
	 */
	public void writeToFile (final Stream <Type> stream, final File file)
		throws IOException
	{
		writeToFile (stream, file, encoding);
	}

	/**
	 * Writes the contents of <code>stream</code> into a <code>String</code>. Each item in the
	 * stream will represent a text line in the output.
	 */
	public String writeToString (final Stream <Type> stream) throws IOException
	{
		return write (stream, new StringWriter ()).toString ();
	}

	///endregion

	///region INHERITABLE METHODS

	/**
	 * Combines the list of <code>fields</code> into one single <code>String</code>.
	 */
	protected abstract String join (final List <String> fields);

	/**
	 * Removes any unnecessary characters from the field value. This is performed right after
	 * pre-processing.
	 */
	protected abstract String clean (final Field field, final String fieldValue);

	///endregion

	///region PRIVATE METHODS

	/**
	 * Converts <code>item</code> into its <code>String</code>, processed, representation.
	 */
	private String mapItem (final Type item)
	{
		final List <String> fields = new ArrayList <> ();

		formattedFields.forEach (field ->
		{
			final FormattedField formattedField = FFUtils.getFormattedField (field);
			final int position = formattedField.position ();

			while (fields.size () < (position - 1))
			{
				fields.add ("");
			}

			try
			{
				Object preprocessed = preprocessor.process (
					FieldUtils.readField (field, item, true),
					position
				);

				String result = "";

				if (!(formattedField.optional () && Objects.isNull (preprocessed)))
				{
					result = postprocessor.process (
						clean (field, Utils.render (field, preprocessed)),
						position
					);
				}

				fields.add (result);
			}

			catch (Exception e)
			{
				e.printStackTrace ();
			}
		});

		return join (fields);
	}

	///endregion
}
