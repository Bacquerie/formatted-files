package org.bff.files.formatted;

import org.apache.commons.lang3.StringUtils;
import org.bff.files.formatted.processors.WritePostprocessor;
import org.bff.files.formatted.processors.WritePreprocessor;
import org.bff.files.utils.FFUtils;
import org.bff.formatted.model.annotations.FormattedField;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * Processes flat-text files with fixed-width columns.
 *
 * @author Alejandro Bacquerie
 * @version 0.0.1
 * @since Java 1.8
 */
public class FixedWidthWriter <Type> extends FormattedWriter <Type>
{
	///region CONSTRUCTORS

	/**
	 * Creates a new writer to process fixed-width text files, and map its lines to the given
	 * <code>klass</code>.
	 */
	public FixedWidthWriter (final Builder <Type> builder)
	{
		super (builder.klass, builder.preprocessor, builder.postprocessor);
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected String clean (final Field field, final String fieldValue)
	{
		FormattedField formatted = FFUtils.getFormattedField (field);

		return formatted.rightAligned () ?
			StringUtils.rightPad (fieldValue, formatted.size (), formatted.filler ()) :
			StringUtils.leftPad (fieldValue, formatted.size (), formatted.filler ());
	}

	@Override
	protected String join (List <String> fields)
	{
		return String.join ("", fields);
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

		private WritePreprocessor preprocessor = (x, i) -> x;
		private WritePostprocessor postprocessor = (x, i) -> x;

		private Builder (final Class <Type> klass)
		{
			this.klass = klass;
		}

		public Builder <Type> preprocessor (final WritePreprocessor preprocessor)
		{
			if (!Objects.isNull (preprocessor)) this.preprocessor = preprocessor;

			return this;
		}

		public Builder <Type> postprocessor (final WritePostprocessor postprocessor)
		{
			if (!Objects.isNull (postprocessor)) this.postprocessor = postprocessor;

			return this;
		}

		public FixedWidthWriter <Type> build ()
		{
			return new FixedWidthWriter <> (this);
		}
	}

	///endregion
}
