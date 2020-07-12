package org.bff.banner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader
{
	private final Properties properties = new Properties ();

	public PropertiesLoader (final File propertiesFile) throws IOException
	{
		properties.load (new FileReader (propertiesFile));
	}

	public Object getOrDefault (Object key, Object defaultValue)
	{
		return properties.getOrDefault (key, defaultValue);
	}
}
