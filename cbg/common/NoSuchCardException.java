package cbg.common;

/**
 * @author Stephen Chudleigh
 */
public class NoSuchCardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for NoSuchCardException.
	 */
	public NoSuchCardException() {
		super();
	}

	/**
	 * Constructor for NoSuchCardException.
	 * @param arg0
	 */
	public NoSuchCardException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor for NoSuchCardException.
	 * @param arg0
	 */
	public NoSuchCardException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for NoSuchCardException.
	 * @param arg0
	 * @param arg1
	 */
	public NoSuchCardException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
