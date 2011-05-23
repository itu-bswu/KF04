package exceptions;

/**
 * An exception thrown if no path can be found.
 * @author mollerhoj3
 */
public class NoPathException extends Exception {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -1254096067550519033L;

	public NoPathException() {
		super();
	}
	
	public NoPathException(String str) {
		super(str);
	}
}
