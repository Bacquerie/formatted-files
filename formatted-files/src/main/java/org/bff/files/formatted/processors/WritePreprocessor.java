package org.bff.files.formatted.processors;

/**
 * Allows for additional transformation of <code>field</code> before being assigned to
 * the respective object field.
 */
@FunctionalInterface
public interface WritePreprocessor
{
	Object process (final Object field, int position);
}
