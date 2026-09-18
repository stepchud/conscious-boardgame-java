/*
 * Created on Oct 21, 2006
 */
package cbg.common;

/**
 * @author Stephen
 */
public class NoSuchNoteException extends Exception {
    /**
	 * Constructor for NoSuchCardException.
	 */
	public NoSuchNoteException() {
		super();
	}

	/**
	 * Constructor for NoSuchNoteException.
	 * @param arg0
	 */
	public NoSuchNoteException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor for NoSuchNoteException.
	 * @param arg0
	 */
	public NoSuchNoteException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor for NoSuchNoteException.
	 * @param arg0
	 * @param arg1
	 */
	public NoSuchNoteException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
