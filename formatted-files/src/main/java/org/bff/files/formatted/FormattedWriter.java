package org.bff.files.formatted;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class FormattedWriter <Type> extends FormattedProcessor <Type>
{
	private static final String lineSeparator = "\n";
	
	/// CONSTRUCTORS
	
	protected FormattedWriter (final Class <?> klass)
	{
		super (klass);
	}
	
	/// OVERRIDABLE METHODS
	
	public void writeToFile (final Stream <Type> stream, File file, Charset encoding)
	{
		try
		{
			write (stream, new FileWriter (file, encoding));
		}
		
		catch (Exception e) { }
	}
	
	public void writeToFile (final Stream <Type> stream, File file)
	{
		writeToFile (stream, file, Charset.forName ("UTF-8"));
	}
	
	public String writeToString (final Stream <Type> stream)
	{
		return write (stream, new StringWriter ()).toString ();
	}
	
	public Writer write (final Stream <Type> stream, Writer writer)
	{
		if (Objects.isNull (stream) || Objects.isNull (writer)) return null;
		
		try
		{
			writer.append (
				String.join (lineSeparator, stream.map (this::mapItem).toArray (String []::new))
			).flush ();
			
			return writer;
		}
		
		catch (IOException e) { return null; }
	}
	
	protected abstract String mapItem (final Type item);
}
