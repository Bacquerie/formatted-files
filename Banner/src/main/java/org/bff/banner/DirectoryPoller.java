package org.bff.banner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.bff.banner.util.Utils;

import javax.crypto.spec.PSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
public class DirectoryPoller extends FileAlterationListenerAdaptor
{
	///region FIELDS

	/**
	 * Internal changes monitor.
	 */
	private final FileAlterationMonitor monitor;

	///endregion

	///region CONSTRUCTORS

	/**
	 * Registers <code>directory</code> for polling at the given <code>pollingInterval</code>,
	 * in millis.
	 */
	public DirectoryPoller (final File directory, long pollingInterval)
	{
		log.debug ("Initializing DirectoryPoller");

		final FileAlterationObserver observer = new FileAlterationObserver (directory);

		observer.addListener (this);

		monitor = new FileAlterationMonitor (pollingInterval);

		monitor.addObserver (observer);

		log.debug ("DirectoryPoller initialized.");
	}

	///endregion

	///region PUBLIC METHODS

	public void run () throws Exception
	{
		log.debug ("Waiting for changes.");

		monitor.start ();
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	public void onFileCreate (final File file)
	{
		log.debug ("Replicating onFileCreate for: " + file);

		writeFile (file);
	}

	@Override
	public void onFileChange (final File file)
	{
		log.debug ("Replicating onFileChange for: " + file);

		writeFile (file);
	}

	@Override
	public void onFileDelete (final File file)
	{
		log.debug ("Replicating onFileDelete for: " + file);

		try
		{
			new BannerReplicator ().replicate (file, DirectoryPoller::deleteFile);
		}

		catch (Exception e)
		{
			log.warn ("", e.fillInStackTrace ());
		}
	}

	///endregion

	private static void deleteFile (final File source, final File target)
	{
		if (target.exists () && target.isFile ())
		{
			target.delete ();
		}
	}

	private void writeFile (final File file)
	{
		try
		{
			final String fileContent = FileUtils.readFileToString (file, StandardCharsets.UTF_8);

			final ReplicationAction writeRA = (source, target) ->
				Utils.writeReplacedFile (
					target.toPath (),
					fileContent,
					source.getName (),
					target.getName ()
				);

			new BannerReplicator ().replicate (file, writeRA);
		}

		catch (Exception e)
		{
			log.warn ("", e.fillInStackTrace ());
		}
	}
}
