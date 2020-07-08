package org.bff.files.formatted.processors;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import org.bff.files.formatted.FFUtils;

public class ParsingUtils
{
	public static Object parse (Field field, String data)
	{
		final Class <?> klass = field.getType ();
		
		Function <String, Object> function;
		
		try
		{
			if ((klass == Character.class) || (klass == char.class)) function = x -> x.charAt (0);
			else if (klass == BigDecimal.class) function = BigDecimal::new;
			else if (klass == BigInteger.class) function = BigInteger::new;
			else if (klass == java.util.Date.class) function = x -> DateParser.parse (FFUtils.getFormattedDate (field), data);
			else function = x -> FFUtils.invokeStaticMethod (klass, "valueOf", x); 
			return function.apply (data);
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
		
		if (klass == java.util.Date.class) return DateParser.render (FFUtils.getFormattedDate (field), data);
		
		return String.valueOf (data);
	}
}
