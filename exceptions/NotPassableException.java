package exceptions;

/**
 * An exception thrown if a road cannot be passed.
 * @author mollerhoj3
 */
public class NotPassableException extends Exception {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 7041061246294149873L;

	public NotPassableException() {
		super();
	}
	
	public NotPassableException(String str) {
		super(str);
	}
}
