package com.sreatt.sreatt_backend.exceptions;

import org.springframework.http.HttpStatus;

public class WarrantyException extends RuntimeException {
	
	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public WarrantyException(String errorCode, String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
