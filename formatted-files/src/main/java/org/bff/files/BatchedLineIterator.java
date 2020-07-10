package org.bff.files;

import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A <code>BatchedReader</code> is a utility class used to read the lines of a text file in batches.
 * Its implementation extends Apache's <code>LineIterator</code>.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public class BatchedLineIterator extends LineIterator
{
	///region FIELDS

	/**
	 * Number of records to fetch in each read operation.
	 */
	private final int batchSize;

	/**
	 * Maximum number of lines to read before closing.
	 */
	private int limit;

	///endregion

	///region CONSTRUCTORS

	/**
	 * For use of the internal Builder.
	 */
	private BatchedLineIterator (final Builder builder)
	{
		super (builder.reader);

		this.batchSize = builder.batchSize;
		this.limit = builder.limit;
	}

	///endregion

	///region PUBLIC METHODS: API

	/**
	 * Reads and returns the next batch of text lines.
	 */
	public Stream <String> read ()
	{
		return read (batchSize).stream ();
	}

	/**
	 * Reads and returns the next batch of text lines, as a <code>Stream</code>.
	 */
	public List <String> readAsList ()
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

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected boolean isValidLine (final String line)
	{
		return !StringUtils.isBlank (line);
	}

	///endregion

	///region PRIVATE METHODS

	/**
	 * Decreases the number of maximum lines to read by one, if possible.
	 */
	private void decreaseLimit ()
	{
		try
		{
			if (!this.hasNext () || (limit <= 0)) return;
			--limit;
			if (limit == 0) close ();
		}

		catch (IOException e)
		{
			e.printStackTrace ();
		}
	}

	/**
	 * Reads and returns the next batch of <code>count</code> text lines, applying a mapping
	 * function to each one. <i>This is the base implementation re-used by its public
	 * variations.</i>.
	 */
	private List <String> read (int count)
	{
		final List <String> lines = new ArrayList <> ();

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

		return lines;
	}

	///endregion

	///region BUILDER

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

		private Builder (final Reader reader)
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

	///endregion
}
