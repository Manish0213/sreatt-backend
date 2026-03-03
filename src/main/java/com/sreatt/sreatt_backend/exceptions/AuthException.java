package com.sreatt.sreatt_backend.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
	
	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public AuthException(String errorCode, String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
}