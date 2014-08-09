/**
 * 
 */
package client.control.session;

/**
 * This exception is used to notify the error manager of a failed operation on the server
 * 
 * @author Peter Abelseth
 * @version 1
 *
 */
public class RequestException extends Exception {
	private static final long serialVersionUID = 32435875L;

	/**
	 * Default constructor of the exception
	 */
	public RequestException() {
	}

	/**
	 * Constructs an exception with an error message
	 * 
	 * @param msg The description of the error
	 */
	public RequestException(String msg) {
		super(msg);
	}

}
