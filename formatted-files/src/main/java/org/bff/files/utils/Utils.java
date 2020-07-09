package org.bff.files.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class Utils
{
	private Utils () { }

	public static boolean isAnyNull (Object ... objects)
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

	public static String removeAll (String string, char c, boolean fromLeft)
	{
		int index = fromLeft ?
			StringUtils.lastIndexOf (string, c) :
			StringUtils.indexOf (string, c);

		if (index == -1) return string;

		return fromLeft ?
			StringUtils.substring (string, index + 1) :
			StringUtils.substring (string, 0, index);
	}
}
