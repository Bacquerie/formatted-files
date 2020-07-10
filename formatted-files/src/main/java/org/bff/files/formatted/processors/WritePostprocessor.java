package org.bff.files.formatted.processors;

/**
 * Allows for additional transformation of <code>field</code> before being converted to
 * its respective target type.
 */
@FunctionalInterface
public interface WritePostprocessor
{
	String process (final String field, int position);
}
