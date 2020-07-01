package org.bff.files.formatted;

import java.lang.reflect.Field;

import org.bff.formatted.model.annotations.FormattedField;

public class FieldParser
{
	public static Object parse (FormattedReader <?> reader, Field field, String rawValue)
	{
		try
		{
			FormattedField formattedField = FFUtils.getFormattedField (field);
			
			String preprocessed = reader.preprocessor.process (rawValue, formattedField.order ());
			
			Object processed = FormattedReader.process (field, preprocessed);
			
			Object posprocessed = reader.postprocessor.process (processed, formattedField.order ());
			
			return posprocessed;
		}
		
		catch (Exception e) { return null; }
	}
}
