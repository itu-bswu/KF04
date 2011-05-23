package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A static class for reading the persistent properties of our program and saving them to the HDD.
 * 
 * @author Niklas Hansen
 */
public class Properties {
	
	private final static String fileName = "config.properties";
	private final static java.util.Properties properties = new java.util.Properties();
	
	/**
	 * Get the stored property with the given key. If the file cannot be 
	 * found, it will get created with default options, and it will try 
	 * to give you the default setting.
	 * BEWARE: If, somehow, the file can never be found, it can go into 
	 * a never-ending loop.
	 * 
	 * @param key Which setting to return
	 * @return The setting-value
	 */
	public static String get (String key) {
		try {
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileName));
			properties.load(is);
			is.close();
			return properties.getProperty(key);
		} catch (FileNotFoundException e) {
			defaultSettings();
			// XXX: Beware of never-ending loop
			return get(key);
		} catch (IOException e) {
			// Return null
		}
		return null;
	}
	
	/**
	 * Set the property with the given key, and set it to the given value.
	 * Remember, in order to save the properties set, call the save()-method.
	 * 
	 * @param key The key to save at.
	 * @param value The value to be saved.
	 */
	public static void set (String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * Save the settings set.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void save () throws FileNotFoundException, IOException {
		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(fileName));
		properties.store(fos, null);
		fos.flush();
		fos.close();
	}
	
	/**
	 * Creates the config-file with the default settings.
	 */
	public static void defaultSettings() {
		properties.setProperty("programName", "Map");
		
		properties.setProperty("dataDir", "data");
		properties.setProperty("nodeFile", "kdv_node_unload.txt");
		properties.setProperty("edgeFile", "kdv_unload.txt");
		
		properties.setProperty("nodeTestFile", "node_test.txt");
		properties.setProperty("edgeTestFile", "edge_test.txt");
		
		properties.setProperty("nodeFileChecksum", "");
		properties.setProperty("dataNodeEdge", "data.dat");
		properties.setProperty("maxBoundsFile", "maxbounds.dat");
		
		try {
			save();
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find settings file.");
		} catch (IOException e) {
			System.err.println("Couldn't write to settings file.");
		}
	}
	
	/**
	 * Resets the properties to the default value.
	 */
	public static void main (String[] args) {
		defaultSettings();
		System.out.println("Properties finished");
	}
}