package org.bff.files.formatted;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.bff.files.formatted.processors.WritePostprocessor;
import org.bff.files.formatted.processors.WritePreprocessor;

/**
 * Processes flat-text files with columns separated by delimiters.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public class DelimiterSeparatedWriter <Type> extends FormattedWriter <Type>
{
	///region FIELDS

	/**
	 * Character used as field delimiter/separator.
	 */
	private final char separator;

	///endregion

	///region CONSTRUCTORS

	/**
	 * For use of the internal Builder.
	 */
	protected DelimiterSeparatedWriter (final Builder <Type> builder)
	{
		super (builder.klass, builder.preprocessor, builder.postprocessor);
		
		this.separator = builder.separator;
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected String clean (final Field field, final String fieldValue)
	{
		return fieldValue;
	}

	@Override
	protected String join (final List <String> fields)
	{
		return String.join (String.valueOf (separator), fields);
	}

	///endregion

	///region BUILDER
	
	public static <Type> Builder <Type> builder (final Class <Type> klass)
	{
		return new Builder <> (klass);
	}
	
	public static final class Builder <Type>
	{
		private final Class <Type> klass;

		private char separator = ',';
		private WritePreprocessor preprocessor = (x, i) -> x;
		private WritePostprocessor postprocessor = (x, i) -> x;
		
		private Builder (final Class <Type> klass)
		{
			this.klass = klass;
		}

		public Builder <Type> separator (char separator)
		{
			if (separator != '\0') this.separator = separator;

			return this;
		}

		public Builder <Type> preprocessor (WritePreprocessor preprocessor)
		{
			if (!Objects.isNull (preprocessor)) this.preprocessor = preprocessor;

			return this;
		}

		public Builder <Type> postprocessor (WritePostprocessor postprocessor)
		{
			if (!Objects.isNull (postprocessor)) this.postprocessor = postprocessor;

			return this;
		}
		
		public DelimiterSeparatedWriter <Type> build ()
		{
			return new DelimiterSeparatedWriter <> (this);
		}
	}

	///endregion
}
