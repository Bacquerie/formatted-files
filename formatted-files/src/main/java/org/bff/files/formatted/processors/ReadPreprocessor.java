package org.bff.files.formatted.processors;

@FunctionalInterface
public interface ReadPreprocessor
{
	String process (String field, int position);
}
