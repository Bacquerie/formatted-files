package org.bff.banner;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Main
{
	private static boolean runDaemon;
	private static File baseBanner;

	public static void main (final String [] args) throws Exception
	{
		parseArgs (args);

		if (runDaemon)
		{
			new DirectoryPoller (baseBanner, 1000).run ();
		}

		else new BannerReplicator ().replicate (baseBanner);
	}

	private static void parseArgs (final String [] args)
	{
		if (args.length == 1)
		{
			baseBanner = new File (args [0]);
		}

		else if (args.length == 2)
		{
			if (!args [0].equals ("-d"))
			{
				System.exit (usage ());
			}

			runDaemon = true;
			baseBanner = new File (args [1]);
		}

		else System.exit (usage ());
	}

	private static int usage ()
	{
		System.out.println ("Usage is: bannerize [-d] <<base_banner_path>>");

		return -1;
	}
}
