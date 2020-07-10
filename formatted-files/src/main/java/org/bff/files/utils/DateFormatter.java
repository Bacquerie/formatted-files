package org.bff.files.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bff.formatted.model.annotations.FormattedDate;

public class DateFormatter
{
	private DateFormatter () { }

	public static Date parse (final FormattedDate field, final String fieldValue)
	{
		try
		{
			return new SimpleDateFormat (field.value ()).parse (fieldValue);
		}
			
		catch (Exception e)
		{
			e.printStackTrace ();

			return null;
		}
	}
	
	public static String render (final FormattedDate field, final Object data)
	{
		try
		{
			return new SimpleDateFormat (field.value ()).format ((Date) data);
		}
		
		catch (Exception e)
		{
			e.printStackTrace ();

			return null;
		}
	}
}
