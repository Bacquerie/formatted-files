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
public class Bannerize
{
	///region FIELDS

	/**
	 * Default charset of the process definition files.
	 */
	private static final Charset defaultCharset = StandardCharsets.UTF_8;

	/**
	 * Process definitions' file extension.
	 */
	private static final String [] pdSuffix = { "process" };

	/**
	 * Arrays of processable banners.
	 */
	private final String [] banners;

	///endregion

	///region CONSTRUCTORS

	/**
	 * Initializes the <code>banners</code> array from a properties file.
	 */
	public Bannerize (final Path path) throws IOException
	{
		if (Objects.isNull (path) || Files.notExists (path))
		{
			throw new IllegalArgumentException ("Invalid properties file.");
		}

		Properties properties = new Properties ();

		properties.load (new FileReader (path.toFile ()));

		this.banners = ((String) properties
			.getOrDefault ("banners", Collections.EMPTY_LIST))
			.split (",");
	}

	///endregion

	///region PUBLIC METHODS: API

	/**
	 * Replicates the process definitions from the given <code>Path</code> into all banners,
	 * in a non-recursive manner, placing them into their respective directories at the same
	 * leven that the given one.
	 */
	public void replicate (final File baseDirectory) throws IOException
	{
		if (Objects.isNull (baseDirectory) || !baseDirectory.exists ())
		{
			throw new IllegalArgumentException ("Invalid arguments.");
		}

		final Map <String, String> filesContent = loadFilesContent (baseDirectory);

		if (filesContent.isEmpty ()) return;

		final String sourceBanner = baseDirectory.getName ();

		getProcessableBanners (baseDirectory)
			.forEach (banner ->
			{
				try
				{
					Files.createDirectories (banner.toPath ());

					filesContent.forEach ((fileName, content) ->
					{
						Path newFile = Paths.get (banner.toString () + "/" + fileName);

						writeReplacedFile (newFile, content, sourceBanner, banner.getName ());
					});
				}

				catch (Exception e)
				{
					e.printStackTrace ();
				}
			});
	}

	///endregion

	/**
	 * Returns a <code>Stream</code> of the banners paths, excluding the base one.
	 */
	private Stream <File> getProcessableBanners (final File baseBanner)
	{
		final String parentDirectory = baseBanner.getParent ();

		return Stream.of (banners)
			.map (banner -> parentDirectory + "/" + Utils.prefixBanner (banner))
			.map (File::new)
			.filter (banner -> !banner.equals (baseBanner));
	}

	/**
	 * Reads the contents of the process definitions under <code>baseDirectory</code>, and
	 * creates a <code>Map</code> with their file names and string contents.
	 */
	private static Map <String, String> loadFilesContent (final File baseDirectory)
		throws IOException
	{
		Map <String, String> filesContent = new HashMap <> ();

		for (File file : FileUtils.listFiles (baseDirectory, pdSuffix, false))
		{
			filesContent.put (file.getName (), FileUtils.readFileToString (file, defaultCharset));
		}

		return filesContent;
	}

	private void writeReplacedFile (Path target, String sourceContents, String from, String to)
	{
		byte [] newContents = sourceContents.replace (from, to).getBytes ();

		try
		{
			Files.write (
				target,
				newContents,
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
