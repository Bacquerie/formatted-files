package org.bff.banner;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class Replicator
{
	///region FIELDS

	/**
	 * Array of directories to replicate actions on.
	 */
	protected final File [] replicas;

	///endregion

	///region CONSTRUCTORS

	/**
	 * Initializes the internal replicas array.
	 */
	public Replicator (Stream <File> replicas)
	{
		if (Objects.isNull (replicas))
		{
			throw new IllegalArgumentException ("Argument cannot be null.");
		}

		this.replicas = replicas
			.filter (replica -> !Objects.isNull (replica))
			.toArray (File []::new);
	}

	///endregion

	///region PUBLIC METHODS: API

	public void replicate (final File file, final ReplicationAction action) throws Exception
	{
		if (Objects.isNull (file) || !file.exists ())
		{
			throw new IllegalArgumentException ("Invalid arguments.");
		}

		for (final File replica : replicas)
		{
			log.debug ("Replicating changes on: " + replica);

			try
			{
				Files.createDirectories (replica.toPath ());

				log.debug ("Directory " + replica + " created.");

				action.execute (file, new File (replica.toString () + "/" + file.getName ()));

				log.debug ("Replication succeeded.");
			}

			catch (Exception e)
			{
				log.warn ("", e.fillInStackTrace ());
			}
		}
	}

	///endregion
}
