package pathfinding;

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
