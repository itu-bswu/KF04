package exceptions;

/**
 * Nothing Close Exeption
 * An exception thrown if there is nothing nearby.
 */
public class NothingCloseException extends Exception {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 7547027204118995260L;

	public NothingCloseException() {
		super();
	}
	
	public NothingCloseException(String str) {
		super(str);
	}
}
