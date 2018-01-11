package com.wherewerks.wmsorder.bean;

public class OrderProcessingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5774333981069091285L;

	public OrderProcessingException() {
		super();
	}

	public OrderProcessingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OrderProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderProcessingException(String message) {
		super(message);
	}

	public OrderProcessingException(Throwable cause) {
		super(cause);
	}



}
