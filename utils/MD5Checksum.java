package utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Class for creating a MD5 checksum of a file.
 * 
 * This is not our code. The code is borrowed from Real's Java-How-to
 * Source: http://www.rgagnon.com/javadetails/java-0416.html
 * 
 * We have changed it slightly, but functionality is the same.
 */
public class MD5Checksum {

	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result.concat(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result;
	}
}