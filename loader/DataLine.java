package loader;

import java.io.IOException;

/**
 * This class parses a line of data that is comma seperated, but where strings
 * can contain commas inside text strings Supporting ',' inside '' is necessary
 * as roadnames can contain commas.
 * 
 * @author Peter Tiedemann petert@itu.dk
 * 
 *         Peter Sestoft 2008: Modified to avoid building and destroying a LinkedList.
 */
public class DataLine {
	private final String line;
	int next;

	public DataLine(String line) {
		this.line = line;
		next = 0;
	}

	/**
	 * Returns true if there are more tokens, and false otherwise
	 */
	public boolean hasMore() {
		return next < line.length();
	}

	private String nextToken() {
		if (line.charAt(next) != '\'') {
			int comma = line.indexOf(',', next);
			String token;
			if (comma >= 0) {
				token = line.substring(next, comma);
				next = comma + 1;
			} else {
				token = line.substring(next);
				next = line.length();
			}
			return token;
		} else {
			int quote = line.indexOf('\'', next + 1);
			String token;
			if (quote >= 0) {
				token = line.substring(next, quote + 1);
				next = quote + 2;
			} else {
				token = line.substring(next);
				next = line.length();
			}
			return token;
		}
	}

	/**
	 * Attempts to parse the next token as an integer,if the token is an empty
	 * string -1 is returned
	 * 
	 * @throws IOException
	 */
	public int getPositiveInt() throws IOException {
		String s = nextToken();
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {

			System.out.println("HER: " + s);

			if (s.equals("''"))
				return -1;
			else
				throw new IOException(s + " is not a integer!");
		}
	}

	/**
	 * Attempts to parse the next token as a double,if the token is an empty
	 * string -1 is returned
	 * 
	 * @throws IOException
	 */
	public float getPositiveFloat() throws IOException {
		String s = nextToken();
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			if (s.equals("''"))
				return -1;
			else
				throw new IOException(s + " is not a Double!");
		}
	}

	/**
	 * Returns the next token as a string, removing ' from beginning and end of
	 * string
	 * 
	 * @return
	 */
	public String getString() {
		String s = nextToken();
		if (s.equals("''"))
			return "";
		else if (s.indexOf('\'') == -1)
			return s;
		else
			return s.substring(1, s.length() - 1);
	}

	/**
	 * Discard the current token
	 * 
	 */
	public void discard() {
		nextToken();
	}

	/**
	 * For debugging
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String line = "A,'',1,'foo,bar',177"; //
		DataLine dl = new DataLine(line);
		while (dl.hasMore()) {
			System.out.println("[" + dl.getString() + "]");
		}
	}
}
