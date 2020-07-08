package org.bff.files.formatted;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.BatchedLineIterator;
import org.bff.files.formatted.processors.ReadPostprocessor;
import org.bff.files.formatted.processors.ReadPreprocessor;
import org.bff.formatted.model.annotations.FormattedField;

/**
 * This class helps read delimiter-separated flat text files in batches, mapping them to any
 * properly annotated class.
 * 
 * @author Alejandro Bacquerie
 * @version 0.0.1
 */
public class DelimiterSeparatedReader <Type> extends FormattedReader <Type>
{
	///region FIELDS

	/**
	 * Internal representation of the delimiter character.
	 */
	private final Pattern pattern;

	///endregion

	///region CONSTRUCTORS
	
	/**
	 * For use of the internal builder.
	 */
	protected DelimiterSeparatedReader(final Builder builder)
	{
		super (builder.klass, builder.lineIterator, builder.preprocessor, builder.postprocessor);
		
		this.pattern = builder.pattern;
	}

	///endregion
	
	///region OVERRIDDEN METHODS
	
	@Override
	@SuppressWarnings ("unchecked")
	protected Type mapLine (final String line)
	{
		try
		{
			final Type classInstance = (Type) ConstructorUtils.invokeConstructor (klass);
			
			final String [] tokens = pattern.split (line);
			
			for (Field field : formattedFields)
			{
				FormattedField formattedField = FFUtils.getFormattedField (field);
				
				if (FFUtils.within (formattedField.order () - 1, 0, tokens.length))
				{
					Object result = FieldParser.parse (
						this,
						field,
						tokens [formattedField.order () - 1]
					);
					
					FieldUtils.writeField (field, classInstance, result, true);
				}
			}
			
			return classInstance;
		}
		
		catch (Exception e) { e.printStackTrace (); return null; }
	}

	///endregion
	
	///region BUILDER
	
	/**
	 * Helper method to create a new batched reader that reads lines from a file.
	 */
	public static Builder builder (final Class <?> klass, final File file, Charset encoding)
		throws IOException
	{
		return builder (new InputStreamReader (new FileInputStream (file), encoding), klass);
	}
	
	/**
	 * Helper method to create a new batched reader that reads lines from a file.
	 */
	public static Builder builder (final Class <?> klass, final File file)
		throws FileNotFoundException
	{
		return builder (new FileReader (file), klass);
	}
	
	/**
	 * Helper method to create a new batched reader that reads lines from a String.
	 */
	public static Builder builder (final Class <?> klass, final String string)
	{
		return builder (new StringReader (string), klass);
	}
	
	/**
	 * Helper method to create a new batched reader that reads lines from a generic
	 * <code>Reader</code>.
	 */
	public static Builder builder (final Reader reader, final Class <?> klass)
	{
		return new Builder (reader, klass);
	}
	
	public static final class Builder
	{
		private final BatchedLineIterator.Builder lineIteratorBuilder;
		private BatchedLineIterator lineIterator;
		
		private final Class <?> klass;
		private ReadPreprocessor preprocessor = (x, i) -> x;
		private ReadPostprocessor postprocessor = (x, i) -> x;
		
		private Pattern pattern;
		private char separator = ',';
		
		private Builder (final Reader reader, final Class <?> klass)
		{
			this.lineIteratorBuilder = BatchedLineIterator.builder (reader);
			this.klass = klass;
		}
		
		/**
		 * Sets the number of lines to fetch from the file in each read operation. Defaults to -1,
		 * which is used to read all lines at once.
		 */
		public Builder batchSize (int batchSize)
		{
			this.lineIteratorBuilder.batchSize (batchSize);
			
			return this;
		}
		
		/**
		 * Sets the maximum number of lines to read before closing the iterator. Default to -1,
		 * which is used to read the file until no more lines can be read.
		 */
		public Builder limit (int limit)
		{
			this.lineIteratorBuilder.limit (limit);
			
			return this;
		}
		
		/**
		 * Sets a mechanism to perform field-level transformations and mappings, after such fields
		 * have been converted to its target data type.
		 */
		public Builder postprocessor (final ReadPostprocessor postprocessor)
		{
			if (!Objects.isNull (postprocessor)) this.postprocessor = postprocessor;
			
			return this;
		}
		
		/**
		 * Sets a mechanism to perform field-level transformations and mappings, while such fields
		 * are still unprocessed strings.
		 */
		public Builder preprocessor (final ReadPreprocessor preprocessor)
		{
			if (!Objects.isNull (preprocessor)) this.preprocessor = preprocessor;
			
			return this;
		}
		
		/** Sets the column delimiter. */
		public Builder separator (final char separator)
		{
			this.separator = separator;
			
			return this;
		}
		
		public DelimiterSeparatedReader <?> build () throws IOException
		{
			lineIterator = lineIteratorBuilder.build ();
			
			this.pattern = Pattern.compile (String.valueOf (separator), Pattern.LITERAL);
			
			return new DelimiterSeparatedReader <> (this);
		}
	}

	///endregion
}
