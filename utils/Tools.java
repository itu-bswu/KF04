package utils;

public class Tools {

	/**
	 * Prints out the amount of RAM currently used (in MegaBytes)
	 */
	private static void printRAM(){
		System.out.println("Used Memory: "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024)+" mb");
	}

}
