package com.wherewerks.wmsorder.dao;

import com.attunedlabs.core.feature.exception.LeapBadRequestException;

public class OrderProcessingDaoException extends LeapBadRequestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1489931475255231333L;


	public OrderProcessingDaoException(String userMessage, Throwable cause, String developerMessage, int errorCode) {
		super(userMessage, cause);
		setUserMessage(userMessage);
		setDeveloperMessage(developerMessage);
		setAppErrorCode(errorCode);
		setStackTrace(cause.getStackTrace());
	}



}
