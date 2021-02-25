package in.onepartner.exception;

/**
 * RecordNotFoundException thrown when a record not found occurred
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class RecordNotFoundException extends Exception {

	/**
	 * @param msg
	 *            error message
	 */
	public RecordNotFoundException(String msg) {
		super(msg);

	}
}
