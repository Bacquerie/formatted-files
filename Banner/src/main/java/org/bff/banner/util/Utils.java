package org.bff.banner.util;

import java.util.Objects;
import java.util.function.Predicate;

public class Utils
{
	private Utils () { }

	public static boolean anyMatches (Predicate <Object> predicate, Object... objects)
	{
		for (Object object : objects)
		{
			if (predicate.test (object))
			{
				return true;
			}
		}

		return false;
	}

	public static String padFront (final String string, final char padChar, final int toSize)
	{
		if (Objects.isNull (string)) return null;

		return repeat (padChar, toSize - string.length ()) + string;
	}

	public static String prefixBanner (String string)
	{
		return "DB" + padFront (string, '0', 3);
	}

	public static String repeat (final char c, int times)
	{
		StringBuilder buffer = new StringBuilder ();

		while (--times >= 0)
		{
			buffer.append (c);
		}

		return buffer.toString ();
	}
}
