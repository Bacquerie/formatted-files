package org.bff.files.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public final class Utils
{
	private Utils () { }

	public static String [] breakAt (final String string, final IntStream breakPoints)
	{
		if (isAnyNull (string, breakPoints)) return new String [] { };

		final List <String> tokens = new ArrayList <> ();
		int last = 0;

		int [] indices = breakPoints
			.distinct ()
			.filter (i -> i > 0)
			.sorted ()
			.toArray ();

		for (int index : indices)
		{
			tokens.add (StringUtils.substring (string, last, index));

			last = index;
		}

		return tokens.toArray (new String [] { });
	}

	@SuppressWarnings ({ "unchecked", "rawtypes" })
	public static Object cast (final Field field, final String data)
	{
		final Class <?> klass = field.getType ();

		if (klass.isEnum ())
		{
			return EnumUtils.getEnum ((Class <Enum>)field.getType (), data);
		}

		if (klass == java.util.Date.class)
		{
			return DateFormatter.parse (FFUtils.getFormattedDate (field), data);
		}

		return ConvertUtils.convert (data, field.getType ());
	}

	public static boolean isAnyNull (final Object ... objects)
	{
		for (Object object : objects)
		{
			if (Objects.isNull (object))
			{
				return true;
			}
		}

		return false;
	}

	public static String render (final Field field, final Object data)
	{
		if (field.getType () == java.util.Date.class)
		{
			return DateFormatter.render (FFUtils.getFormattedDate (field), data);
		}

		return String.valueOf (data);
	}

	public static String removeAll (final String string, char c, boolean fromLeft)
	{
		int index = fromLeft ?
			StringUtils.lastIndexOf (string, c) :
			StringUtils.indexOf (string, c);

		if (index == -1) return string;

		return fromLeft ?
			StringUtils.substring (string, index + 1) :
			StringUtils.substring (string, 0, index);
	}

	public static boolean within (int index, int min, int max)
	{
		return (index >= min) && (index < max);
	}
}
