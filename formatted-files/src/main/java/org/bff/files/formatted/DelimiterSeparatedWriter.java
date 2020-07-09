package org.bff.files.formatted;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.utils.ParsingUtils;
import org.bff.files.utils.FFUtils;
import org.bff.formatted.model.annotations.FormattedField;

public class DelimiterSeparatedWriter <Type> extends FormattedWriter <Type>
{
	///region FIELDS

	/**
	 * Character used as field delimiter.
	 */
	private final char separator;

	///endregion

	///region CONSTRUCTORS

	/**
	 * For use of the internal Builder.
	 */
	protected DelimiterSeparatedWriter (final Builder <Type> builder)
	{
		super (builder.klass);
		
		this.separator = builder.separator;
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected String mapItem (final Type item)
	{
		final List <String> fields = new ArrayList <> ();
		
		formattedFields.forEach (field ->
		{
			final FormattedField formattedField = FFUtils.getFormattedField (field);
			
			while (fields.size () < (formattedField.order () - 1))
			{
				fields.add ("");
			}
			
			try
			{
				Object fieldValue = FieldUtils.readField (field, item, true);

				fields.add (ParsingUtils.render (field, fieldValue));
			}
			
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		});
		
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
		
		private Builder (final Class <Type> klass)
		{
			this.klass = klass;
		}
		
		public Builder <Type> separator (char separator)
		{
			if (separator != '\0') this.separator = separator;
			
			return this;
		}
		
		public DelimiterSeparatedWriter <Type> build ()
		{
			return new DelimiterSeparatedWriter <> (this);
		}
	}

	///endregion
}
