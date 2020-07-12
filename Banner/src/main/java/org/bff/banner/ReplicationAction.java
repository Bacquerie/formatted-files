package org.bff.banner;

import java.io.File;

@FunctionalInterface
public interface ReplicationAction
{
	/**
	 * Action to be replicated on files loaded by <code>Replicator</code>.
	 *
	 * @param source File that originated the replication.
	 * @param target File where the action will be replicated.
	 */
	void execute (final File source, final File target);
}
