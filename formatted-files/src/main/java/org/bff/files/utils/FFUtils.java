package org.bff.files.utils;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

public final class FFUtils
{
	private FFUtils () { }
	
	public static FormattedDate getFormattedDate (final Field field)
	{
		return field.getAnnotation (FormattedDate.class);
	}
	
	public static FormattedField getFormattedField (final Field field)
	{
		return field.getAnnotation (FormattedField.class);
	}

	public static List <Field> getFormattedFields (final Class <?> klass)
	{
		final List <Field> fields = FieldUtils.getFieldsListWithAnnotation (
			klass,
			FormattedField.class
		);

		fields.sort (Comparator.comparingInt (x -> getFormattedField (x).position ()));

		return fields;
	}
}
