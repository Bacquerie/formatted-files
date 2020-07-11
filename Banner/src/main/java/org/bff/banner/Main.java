package org.bff.banner;

import java.io.File;
import java.nio.file.Paths;

public class Main
{
	public static String basePath =
		"/data/workspace/designer/uvm/BWAltaDocentesV1_0_bannerize/BWAltaDocentesV1_0/BusinessProcesses/ServiceOperations/Banner/DB001";

	public static void main (final String [] args) throws Exception
	{
		File baseDirectory = new File (basePath);

		new Bannerize (Paths.get ("banner.properties")).replicate (baseDirectory);
	}
}
