package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
			properties.load(new FileInputStream(fileName));
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
	 * @param key
	 * @param value
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
		properties.store(new FileOutputStream(fileName), null);
	}
	
	/**
	 * Creates the config file with the default settings.
	 */
	public static void defaultSettings() {
		properties.setProperty("dataDir", "data");
		properties.setProperty("nodeFile", "kdv_node_unload.txt");
		properties.setProperty("edgeFile", "kdv_unload.txt");
		
		properties.setProperty("nodeFileChecksum", "");
		properties.setProperty("quadTreeFile", "quadtree.dat");
		properties.setProperty("maxBoundsFile", "maxbounds.dat");
		
		try {
			properties.store(new FileOutputStream(fileName), null);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find settings file.");
		} catch (IOException e) {
			System.err.println("Couldn't write to settings file.");
		}
	}
	
	public static void main (String[] args) {
		defaultSettings();
	}
}
