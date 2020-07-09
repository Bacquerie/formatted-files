package org.bff.files.formatted.reader;

import java.lang.reflect.Field;

import org.bff.files.utils.FFUtils;
import org.bff.files.utils.ParsingUtils;
import org.bff.formatted.model.annotations.FormattedField;

public final class FieldParser
{
	private FieldParser () { }

	public static <Type> Object parse (FormattedReader <Type> reader, Field field, String rawValue)
	{
		try
		{
			FormattedField formattedField = FFUtils.getFormattedField (field);
			
			String preprocessed = reader.preprocessor.process (rawValue, formattedField.order ());

			Object processed = ParsingUtils.cast (field, preprocessed);

			return reader.postprocessor.process (processed, formattedField.order ());
		}
		
		catch (Exception e) { return null; }
	}
}
