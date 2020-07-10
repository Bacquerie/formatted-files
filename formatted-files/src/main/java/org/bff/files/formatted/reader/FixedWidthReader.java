package org.bff.files.formatted.reader;

import org.bff.files.BatchedLineIterator;
import org.bff.files.formatted.processors.ReadPostprocessor;
import org.bff.files.formatted.processors.ReadPreprocessor;
import org.bff.files.utils.FFUtils;
import org.bff.files.utils.Utils;
import org.bff.formatted.model.annotations.FormattedField;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class FixedWidthReader <Type> extends FormattedReader <Type>
{
	///region CONSTRUCTORS

	/**
	 * For use of the internal builder.
	 */
	protected FixedWidthReader (final Builder <Type> builder)
	{
		super (builder.klass, builder.lineIterator, builder.preprocessor, builder.postprocessor);
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected String clean (final Field field, final String fieldValue)
	{
		FormattedField formatted = FFUtils.getFormattedField (field);

		return Utils.removeAll (fieldValue, formatted.filler (), formatted.rightAligned ());
	}

	@Override
	protected List <String> tokenize (final String line)
	{
		final IntStream.Builder builder = IntStream.builder ();

		int start = 0;

		for (Field field : formattedFields)
		{
			builder.add (start);

			start += FFUtils.getFormattedField (field).size ();
		}

		return Arrays.asList (Utils.breakAt (line, builder.add (start).build ()));
	}

	///endregion

	///region BUILDER

	/**
	 * Helper method to create a new batched reader that reads lines from a file.
	 */
	public static <Type> Builder<Type> builder (final Class <Type> klass, final File file, Charset encoding)
		throws IOException
	{
		return builder (klass, new InputStreamReader (new FileInputStream (file), encoding));
	}

	/**
	 * Helper method to create a new batched reader that reads lines from a file.
	 */
	public static <Type> Builder<Type> builder (final Class <Type> klass, final File file)
		throws FileNotFoundException
	{
		return builder (klass, new FileReader (file));
	}

	/**
	 * Helper method to create a new batched reader that reads lines from a String.
	 */
	public static <Type> Builder<Type> builder (final Class <Type> klass, final String string)
	{
		return builder (klass, new StringReader (string));
	}

	/**
	 * Helper method to create a new batched reader that reads lines from a generic
	 * <code>Reader</code>.
	 */
	public static <Type> Builder<Type> builder (final Class <Type> klass, final Reader reader)
	{
		return new Builder<> (reader, klass);
	}

	public static final class Builder <Type>
	{
		private final BatchedLineIterator.Builder lineIteratorBuilder;
		private BatchedLineIterator lineIterator;

		private final Class<Type> klass;
		private ReadPreprocessor preprocessor = (x, i) -> x;
		private ReadPostprocessor postprocessor = (x, i) -> x;

		private Builder (final Reader reader, final Class<Type> klass)
		{
			this.lineIteratorBuilder = BatchedLineIterator.builder (reader);
			this.klass = klass;
		}

		/**
		 * Sets the number of lines to fetch from the file in each read operation. Defaults to -1,
		 * which is used to read all lines at once.
		 */
		public Builder<Type> batchSize (int batchSize)
		{
			this.lineIteratorBuilder.batchSize (batchSize);

			return this;
		}

		/**
		 * Sets the maximum number of lines to read before closing the iterator. Default to -1,
		 * which is used to read the file until no more lines can be read.
		 */
		public Builder<Type> limit (int limit)
		{
			this.lineIteratorBuilder.limit (limit);

			return this;
		}

		/**
		 * Sets a mechanism to perform field-level transformations and mappings, after such fields
		 * have been converted to its target data type.
		 */
		public Builder<Type> postprocessor (ReadPostprocessor postprocessor)
		{
			if (!Objects.isNull (postprocessor)) this.postprocessor = postprocessor;

			return this;
		}

		/**
		 * Sets a mechanism to perform field-level transformations and mappings, while such fields
		 * are still unprocessed strings.
		 */
		public Builder<Type> preprocessor (ReadPreprocessor preprocessor)
		{
			if (!Objects.isNull (preprocessor)) this.preprocessor = preprocessor;

			return this;
		}

		public FixedWidthReader<Type> build () throws IOException
		{
			lineIterator = lineIteratorBuilder.build ();

			return new FixedWidthReader <Type> (this);
		}
	}

	///endregion
}
