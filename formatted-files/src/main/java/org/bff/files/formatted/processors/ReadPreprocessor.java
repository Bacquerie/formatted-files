package org.bff.files.formatted.processors;

@FunctionalInterface
public interface ReadPreprocessor
{
	/**
	 * Allows for additional transformation of <code>field</code> before being converted to
	 * its respective target type.
	 */
	String process (final String field, int position);
}
