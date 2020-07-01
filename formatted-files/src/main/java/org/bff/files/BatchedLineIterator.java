package org.bff.files;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.io.LineIterator;

/**
 * A <code>BatchedReader</code> is a utility class used to read the lines of a text file in batches.
 * Its implementation extends Apache's <code>LineIterator</code>.
 * 
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 8
 */
public class BatchedLineIterator extends LineIterator
{
	/// PRIVATE FIELDS
	
	/**
	 * Number of records to fetch in each read operation. 
	 */
	private final int batchSize;
	
	/**
	 * Maximum number of lines to read before closing.
	 */
	private int limit;
	
	/// CONSTRUCTORS
	
	/**
	 * For use of the internal Builder.
	 */
	private BatchedLineIterator(Builder builder)
	{
		super(builder.reader);
		
		this.batchSize = builder.batchSize;
		this.limit = builder.limit;
	}
	
	/// PUBLIC API
	
	/**
	 * Reads and returns the next batch of text lines.
	 */
	public Stream <String> read ()
	{
		return read (batchSize);
	}
	
	/**
	 * Ignores the next <code>count</code> lines from the file.
	 */
	public void skip (int count)
	{
		if (count >= 1) read (count);
	}
	
	/// OVERRIDDEN METHODS
	
	@Override
	protected boolean isValidLine (final String line)
	{
		return !Objects.isNull (line) && !line.isBlank ();
	}
	
	
	/// PRIVATE METHODS
	
	/**
	 * Decreases the number of maximum lines to read by one, if possible.
	 */
	private void decreaseLimit ()
	{
		try
		{
			if (!this.hasNext () || (limit <= 0)) return;
			if (limit > 0) --limit;
			if (limit == 0) close ();
		}
		
		catch (Exception e) { }
	}
	
	/**
	 * Reads and returns the next batch of <code>count</code> text lines, applying a mapping
	 * function to each one. <i>This is the base implementation re-used by its public
	 * variations.</i>.
	 */
	private Stream <String> read (int count)
	{
		final Stream.Builder <String> lines = Stream.builder ();
		
		while ((count < 1) && hasNext ())
		{
			lines.add (nextLine ());
			
			decreaseLimit ();
		}
		
		while ((--count >= 0) && hasNext ())
		{
			lines.add (nextLine ());
			
			decreaseLimit ();
		}
		
		return lines.build ();
	}
	
	/// BUILDER
	
	/**
	 * Helper method to create <code>BatchedReader</code> instances that reads lines from a
	 * generic <code>Reader</code>.
	 */
	public static Builder builder (final Reader reader)
	{
		return new Builder (reader);
	}
	
	/**
	 * Helper class in charge of creating <code>BatchedReader</code> instances.
	 */
	public static final class Builder
	{
		private final Reader reader;
		private int batchSize = -1;
		private int limit = -1;
		
		private Builder(final Reader reader)
		{
			this.reader = reader;
		}
		
		/**
		 * Sets the number of lines to fetch from the file in each read operation. Defaults to -1,
		 * which is used to read all lines at once.
		 */
		public Builder batchSize (int batchSize)
		{
			if (batchSize > 0) this.batchSize = batchSize;
			
			return this;
		}
		
		/**
		 * Sets the maximum number of lines to read before closing the iterator. Default to -1,
		 * which is used to read the file until no more lines can be read.
		 */
		public Builder limit (int limit)
		{
			if (limit > 0) this.limit = limit;
			
			return this;
		}
		
		/**
		 * Generates a new <code>BatchedReader</code> instance.
		 * 
		 * @throws IOException if an error occurs while creating the instance. This may be due to an
		 *                     incorrect file encoding.
		 */
		public BatchedLineIterator build () throws IOException
		{
			return new BatchedLineIterator (this);
		}
	}
}
