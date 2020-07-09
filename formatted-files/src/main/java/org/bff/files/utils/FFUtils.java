package org.bff.files.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

public final class FFUtils
{
	private FFUtils () { }

	public static List <Field> findFormattedFields (Class <?> klass)
	{
		List <Field> fields = FieldUtils.getFieldsListWithAnnotation (klass, FormattedField.class);

		fields.sort (FFUtils::compare);

		return fields;
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

	public static String padAndTrimField (Field field, Object fieldValue)
	{
		FormattedField formattedField = FFUtils.getFormattedField (field);

		if (formattedField.textAlignment () == FormattedField.Alignment.LEFT)
		{
			return StringUtils.rightPad (
				ParsingUtils.render (field, fieldValue),
				formattedField.width (),
				formattedField.filler ()
			);
		}

		return StringUtils.leftPad (
			ParsingUtils.render (field, fieldValue),
			formattedField.width (),
			formattedField.filler ()
		);
	}

	private static int compare (Field a, Field b)
	{
		return getFormattedField (a).order () > getFormattedField (b).order () ? 1 : -1;
	}
}
