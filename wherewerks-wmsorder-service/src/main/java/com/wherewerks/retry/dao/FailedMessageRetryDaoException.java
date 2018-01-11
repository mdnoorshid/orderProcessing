package com.wherewerks.retry.dao;

public class FailedMessageRetryDaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -827694360817411278L;

	public FailedMessageRetryDaoException() {
		super();
	}

	public FailedMessageRetryDaoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedMessageRetryDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedMessageRetryDaoException(String message) {
		super(message);
	}

	public FailedMessageRetryDaoException(Throwable cause) {
		super(cause);
	}

	
}
