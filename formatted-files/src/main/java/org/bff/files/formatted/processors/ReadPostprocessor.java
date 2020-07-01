package org.bff.files.formatted.processors;

@FunctionalInterface
public interface ReadPostprocessor
{
	Object process (Object field, int position);
}
