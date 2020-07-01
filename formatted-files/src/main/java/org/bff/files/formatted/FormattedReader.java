package org.bff.files.formatted;

import java.util.stream.Stream;

import org.bff.files.BatchedLineIterator;
import org.bff.files.formatted.processors.ReadPostprocessor;
import org.bff.files.formatted.processors.ReadPreprocessor;

public abstract class FormattedReader <Type> extends FormattedProcessor <Type>
{
	protected final BatchedLineIterator lineIterator;
	protected final ReadPreprocessor preprocessor;
	protected final ReadPostprocessor postprocessor;
	
	/// CONSTRUCTORS
	
	protected FormattedReader (
		final Class <?> klass,
		final BatchedLineIterator lineIterator,
		final ReadPreprocessor preprocessor,
		final ReadPostprocessor postprocessor)
	{
		super (klass);
		
		this.lineIterator = lineIterator;
		this.preprocessor = preprocessor;
		this.postprocessor = postprocessor;
	}
	
	/// PUBLIC METHODS
	
	/** Force-closes the reader. **/
	public void close ()
	{
		try { lineIterator.close (); }
		
		catch (Exception e) {}
	}
	
	/** <code>true</code> if there are more lines to read. **/
	public boolean hasNext ()
	{
		return lineIterator.hasNext ();
	}
	
	/** Reads the following batch of lines. **/
	public Stream <Type> read ()
	{
		return lineIterator.read ().map (this::mapLine);
	}
	
	/** Skips the next <code>skipCount</code> number of lines. **/
	public void skip (int skipCount)
	{
		lineIterator.skip (skipCount);
	}
	
	/// OVERRIDABLE METHODS
	
	protected abstract Type mapLine (final String line);
}
