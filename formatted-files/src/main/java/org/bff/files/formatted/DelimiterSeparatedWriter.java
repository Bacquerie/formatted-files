package org.bff.files.formatted;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.formatted.processors.ParsingUtils;
import org.bff.formatted.model.annotations.FormattedField;

public final class DelimiterSeparatedWriter <Type> extends FormattedWriter <Type>
{
	private final char separator;
	
	protected DelimiterSeparatedWriter(final Builder builder)
	{
		super (builder.klass);
		
		this.separator = builder.separator;
	}
	
	@Override
	protected String mapItem (final Type item)
	{
		final List <String> tokens = new ArrayList <> ();
		
		for (Field field : formattedFields)
		{
			FormattedField formattedField = FFUtils.getFormattedField (field);
			
			while (tokens.size () < (formattedField.order ())) tokens.add ("");
			
			try
			{
				tokens.add (ParsingUtils.render (field, FieldUtils.readField (field, item, true)));
			}
			
			catch (Exception e) {}
		}
		
		return String.join (String.valueOf (separator), tokens);
	}
	
	/// BUILDER
	
	public static Builder builder (final Class <?> klass)
	{
		return new Builder (klass);
	}
	
	public static final class Builder
	{
		private final Class <?> klass;
		private char separator = ',';
		
		private Builder(final Class <?> klass)
		{
			this.klass = klass;
		}
		
		public Builder separator (char separator)
		{
			if (separator != '\0') this.separator = separator;
			
			return this;
		}
		
		public DelimiterSeparatedWriter <?> build ()
		{
			return new DelimiterSeparatedWriter <> (this);
		}
	}
}
