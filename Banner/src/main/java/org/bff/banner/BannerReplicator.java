package org.bff.banner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bff.banner.util.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class BannerReplicator extends Replicator
{
	///region FIELDS

	/**
	 * Properties file name.
	 */
	private static final File propertiesFile = new File ("banner.properties");

	///endregion

	///region CONSTRUCTORS

	/**
	 * Initializes the <code>banners</code> array from a properties file.
	 */
	public BannerReplicator () throws IOException
	{
		super (Stream
			.of (((String) new PropertiesLoader (propertiesFile)
			.getOrDefault ("banners", Collections.EMPTY_LIST))
			.split (","))
			.map (File::new)
		);

		log.debug ("Properties loaded.");
	}

	///endregion

	///region PUBLIC METHODS: API

	/**
	 * Replicates the process definitions from the given <code>Path</code> into all banners,
	 * in a non-recursive manner, placing them into their respective directories at the same
	 * leven that the given one.
	 */
	public void replicateAll (final File baseBanner) throws IOException
	{
		if (Objects.isNull (baseBanner) || !baseBanner.exists ())
		{
			throw new IllegalArgumentException ("Invalid arguments.");
		}

		Files.list (baseBanner)
	}

	///endregion
}
