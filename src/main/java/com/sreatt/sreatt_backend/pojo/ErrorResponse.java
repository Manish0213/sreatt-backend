package com.sreatt.sreatt_backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
	private String errorCode;
	private String errorMessage;
}
