/**
 * 
 */
package com.wuweibi.bullet.client.network.wol.exceptions;

/**
 * @author rmrodrigues
 * 
 */
public class UnableToWakeUpWOLNodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8276665758554295391L;

	public UnableToWakeUpWOLNodeException() {
		super();
	}

	public UnableToWakeUpWOLNodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToWakeUpWOLNodeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new unable to wake up wol node exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public UnableToWakeUpWOLNodeException(Throwable cause) {
		super(cause);
	}

}
