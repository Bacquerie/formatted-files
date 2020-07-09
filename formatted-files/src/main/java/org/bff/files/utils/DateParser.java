package org.bff.files.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bff.formatted.model.annotations.FormattedDate;

public class DateParser
{
	public static Date parse (FormattedDate field, String data)
	{
		List <String> formats = new ArrayList <> ();
		
		if (field == null) formats.add ("yyyy-MM-dd");
		
		else
		{
			formats.add (field.value ());
			formats.addAll (Arrays.asList (field.additionalFormats ()));
		}
		
		for (String format : formats)
		{
			try
			{
				return new SimpleDateFormat (format).parse (data);
			}
			
			catch (Exception e) {  }
		}
		
		return null;
	}
	
	public static String render (FormattedDate field, Object data)
	{
		try
		{
			return new SimpleDateFormat (field.value ()).format ((Date) data);
		}
		
		catch (Exception e) { return null; }
	}
}
