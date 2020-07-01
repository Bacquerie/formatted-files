package org.bff.files.formatted;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.bff.files.formatted.processors.ParsingUtils;

public abstract class FormattedProcessor <Type>
{
	protected final Class <?> klass;
	
	protected final List <Field> formattedFields;
	
	protected FormattedProcessor (final Class <?> klass)
	{
		if (Objects.isNull (klass))
		{
			throw new IllegalArgumentException ("Invalid class.");
		}
		
		this.klass = klass;
		this.formattedFields = FFUtils.findFormattedFields (klass);
	}
	
	/// PROTECTED METHODS
	
	protected static Object process (final Field field, final String value)
	{
		return ParsingUtils.parse (field, value);
	}
}
