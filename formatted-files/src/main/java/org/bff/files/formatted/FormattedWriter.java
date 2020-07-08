package org.bff.files.formatted;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
			OutputStreamWriter osw =new OutputStreamWriter (new FileOutputStream (file), encoding);

			write (stream, osw);
		}
		
		catch (Exception e) { }
	}
	
	public void writeToFile (final Stream <Type> stream, File file)
	{
		writeToFile (stream, file, StandardCharsets.UTF_8);
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
