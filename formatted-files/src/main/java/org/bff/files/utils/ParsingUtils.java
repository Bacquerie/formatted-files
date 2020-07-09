package org.bff.files.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public final class ParsingUtils
{
	private ParsingUtils () { }

	public static Object cast (Field field, String data)
	{
		final Class <?> klass = field.getType ();
		
		try
		{
			if ((klass == Character.class) || (klass == char.class))
			{
				return data.charAt (0);
			}

			if (klass == BigDecimal.class)
			{
				return new BigDecimal (data);
			}

			if (klass == BigInteger.class)
			{
				return new BigInteger (data);
			}

			if (klass == java.util.Date.class)
			{
				return DateParser.parse (FFUtils.getFormattedDate (field), data);
			}

			return FFUtils.invokeStaticMethod (klass, "valueOf", data);
		}
		
		catch (Exception e)
		{
			if (!klass.isPrimitive ()) return null;

			if (klass == boolean.class) return false;

			if (klass == char.class) return '\0';

			return 0;
		}
	}
	
	public static String render (Field field, Object data)
	{
		Class <?> klass = field.getType ();
		
		if (klass == java.util.Date.class)
		{
			return DateParser.render (FFUtils.getFormattedDate (field), data);
		}
		
		return String.valueOf (data);
	}
}
