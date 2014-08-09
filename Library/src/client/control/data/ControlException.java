package client.control.data;

/**
 * This exception is used to notify the error manager of a control object's violation of permitted operations
 * 
 * @author Jeremy Lerner
 * @version 1
 *
 */
public class ControlException extends Exception {
	private static final long serialVersionUID = 32435875L;

	/**
	 * Default constructor of the exception
	 */
	public ControlException() {
	}

	/**
	 * Constructs an exception with an error message
	 * 
	 * @param msg The description of the error
	 */
	public ControlException(String msg) {
		super(msg);
	}

}
