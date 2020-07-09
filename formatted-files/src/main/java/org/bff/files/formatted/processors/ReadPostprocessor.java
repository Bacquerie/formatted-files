package org.bff.files.formatted.processors;

@FunctionalInterface
public interface ReadPostprocessor
{
	/**
	 * Allows for additional transformation of <code>field</code> before being assigned to
	 * the respective object field.
	 */
	Object process (final Object field, int position);
}
