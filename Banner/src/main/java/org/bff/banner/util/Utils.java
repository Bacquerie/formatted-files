package org.bff.banner.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class Utils
{
	private Utils () { }

	public static String prefixBanner (final String string)
	{
		if (Objects.isNull (string)) return null;

		return "DB" + repeat ('0', 3 - string.length ()) + string;
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

	/**
	 * Writes the <code>sourceContents</code> into the <code>target</code> file, replacing all
	 * occurrences of <code>from</code> to <code>to</code>.
	 */
	public static void writeReplacedFile (Path target, String sourceContent, String from, String to)
	{
		final byte [] newContent = sourceContent.replace (from, to).getBytes ();

		try
		{
			Files.write (
				target,
				newContent,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE
			);
		}

		catch (IOException e)
		{
			e.printStackTrace ();
		}
	}
}
