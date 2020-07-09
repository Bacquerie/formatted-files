package org.bff.files.formatted;

import org.bff.files.utils.FFUtils;
import org.bff.files.utils.Utils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Provides a base for classes that writes objects into writers as formatted text lines.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public abstract class FormattedWriter <Type>
{
	///region FIELDS

	/**
	 * Line separator to use when writing
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
	 * List of fields from <code>klass</code> annotated as <code>@FormattedField</code>.
	 */
	protected final List <Field> formattedFields;

	///endregion

	///region CONSTRUCTORS

	/**
	 * Intended to be used by derived classes, to initialize the klass and formattedFields
	 * instance attributes.
	 */
	protected FormattedWriter (final Class <Type> klass)
	{
		if (Objects.isNull (klass))
		{
			throw new IllegalArgumentException ("Invalid class");
		}

		this.klass = klass;
		this.formattedFields = FFUtils.findFormattedFields (klass);
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
	 * Converts <code>item</code> into its <code>String</code>, processed, representation.
	 */
	protected abstract String mapItem (final Type item);

	///endregion
}
