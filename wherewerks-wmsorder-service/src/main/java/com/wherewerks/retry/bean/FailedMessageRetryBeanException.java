package com.wherewerks.retry.bean;

public class FailedMessageRetryBeanException extends Exception  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469875041630764856L;

	public FailedMessageRetryBeanException() {
		super();
	}

	public FailedMessageRetryBeanException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedMessageRetryBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedMessageRetryBeanException(String message) {
		super(message);
	}

	public FailedMessageRetryBeanException(Throwable cause) {
		super(cause);
	}


}
