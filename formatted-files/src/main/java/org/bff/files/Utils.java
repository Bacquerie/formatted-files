package org.bff.files;

import java.util.Objects;

public class Utils
{
	public static boolean isBlank (String string)
	{
		return Objects.isNull (string) || (string.trim ().length () == 0);
	}
}
