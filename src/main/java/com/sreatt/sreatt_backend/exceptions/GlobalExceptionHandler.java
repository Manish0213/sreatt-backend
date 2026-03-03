package com.sreatt.sreatt_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.sreatt.sreatt_backend.constant.ErrorCodeEnum;
import com.sreatt.sreatt_backend.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
		// NoResourceFoundException
		@ExceptionHandler(NoResourceFoundException.class)
		public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
			log.error("Handling NoResourceFoundException: {}", ex.getMessage(), ex);
			
			ErrorResponse error = new ErrorResponse(
					ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorCode(),
					ErrorCodeEnum.RESOURCE_NOT_FOUND.getErrorMessage()
					);
			
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
		
		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorResponse> handleException(Exception ex) {
			log.error("Handling generic Exception: {}", ex.getMessage(), ex);
			
			ErrorResponse error = new ErrorResponse(
					ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
					ErrorCodeEnum.GENERIC_ERROR.getErrorMessage()
					);
			
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		@ExceptionHandler(AuthException.class)
	    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {

	        ErrorResponse response = new ErrorResponse(
	            ex.getErrorCode(),
	            ex.getErrorMessage()
	        );

	        return ResponseEntity
	                .status(ex.getHttpStatus())
	                .body(response);
	    }
		
		@ExceptionHandler(ProductException.class)
	    public ResponseEntity<ErrorResponse> handleProductException(ProductException ex) {

	        ErrorResponse response = new ErrorResponse(
	            ex.getErrorCode(),
	            ex.getErrorMessage()
	        );

	        return ResponseEntity
	                .status(ex.getHttpStatus())
	                .body(response);
	    }
		
		@ExceptionHandler(DistributorException.class)
		public ResponseEntity<ErrorResponse> handleDistributorException(DistributorException ex) {

		    ErrorResponse response = new ErrorResponse(
		            ex.getErrorCode(),
		            ex.getErrorMessage()
		    );

		    return ResponseEntity
		            .status(ex.getHttpStatus())
		            .body(response);
		}
		
		@ExceptionHandler(WarrantyException.class)
		public ResponseEntity<ErrorResponse> handleWarrantyException(WarrantyException ex) {

		    ErrorResponse response = new ErrorResponse(
		            ex.getErrorCode(),
		            ex.getErrorMessage()
		    );

		    return ResponseEntity
		            .status(ex.getHttpStatus())
		            .body(response);
		}
}


//@ExceptionHandler(RuntimeException.class)
//public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
//
//    ErrorResponse error = new ErrorResponse(
//            "USER_EXISTS",
//            ex.getMessage()
//    );
//
//    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//}