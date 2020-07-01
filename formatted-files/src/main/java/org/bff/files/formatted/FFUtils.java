package org.bff.files.formatted;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

public final class FFUtils
{
	public static List <Field> findFormattedFields (Class <?> klass)
	{
		return FieldUtils.getFieldsListWithAnnotation (klass, FormattedField.class);
	}
	
	public static FormattedDate getFormattedDate (final Field field)
	{
		return field.getAnnotation (FormattedDate.class);
	}
	
	public static FormattedField getFormattedField (final Field field)
	{
		return field.getAnnotation (FormattedField.class);
	}
	
	public static boolean within (int index, int min, int max)
	{
		return (index >= min) && (index < max);
	}
	
	public static Object invokeStaticMethod (Class <?> klass, String name, Object... params)
	{
		try
		{
			return MethodUtils.invokeStaticMethod (klass, name, params);
		}
		
		catch (Exception e) { return null; }
	}
}
