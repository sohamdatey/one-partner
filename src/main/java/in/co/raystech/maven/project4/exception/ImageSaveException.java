package in.co.raystech.maven.project4.exception;

/**
 * DuplicateRecordException thrown when a duplicate record occurred
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class ImageSaveException extends Exception {

	/**
	 * @param msg
	 *            error message
	 */
	public ImageSaveException(String msg) {
		super(msg);
	}
	public ImageSaveException(Exception e) {
		super(e);
	}

}
