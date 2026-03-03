package com.sreatt.sreatt_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sreatt.sreatt_backend.constant.ErrorCodeEnum;
import com.sreatt.sreatt_backend.dto.ProductCreateRequest;
import com.sreatt.sreatt_backend.exceptions.ProductException;

@Service
public class RequestValidator {

	public void validateProductCreateRequest(ProductCreateRequest request) {
		
		if(request == null) {
			throw new ProductException(
	                ErrorCodeEnum.INVALID_REQUEST_DATA.getErrorCode(),
	                ErrorCodeEnum.INVALID_REQUEST_DATA.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getName() == null || request.getName().isEmpty()) {
			throw new ProductException(
	                ErrorCodeEnum.INVALID_PRODUCT_NAME.getErrorCode(),
	                ErrorCodeEnum.INVALID_PRODUCT_NAME.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getPrice() == null || request.getPrice() <= 0) {
			throw new ProductException(
	                ErrorCodeEnum.INVALID_PRODUCT_PRICE.getErrorCode(),
	                ErrorCodeEnum.INVALID_PRODUCT_PRICE.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		if (request.getSerialNo() == null || request.getSerialNo().trim().isEmpty()) {
			throw new ProductException(
	                ErrorCodeEnum.INVALID_SERIAL_NUMBER.getErrorCode(),
	                ErrorCodeEnum.INVALID_SERIAL_NUMBER.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getBrandId() == null) {
			throw new ProductException(
					ErrorCodeEnum.BRAND_ID_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.BRAND_ID_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getBatteryChemistryId() == null) {
			throw new ProductException(
	             	ErrorCodeEnum.BATTERY_CHEMISTRY_ID_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.BATTERY_CHEMISTRY_ID_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getVehicleTypeIds() == null || request.getVehicleTypeIds().isEmpty()) {
			throw new ProductException(
	                ErrorCodeEnum.VEHICLE_TYPE_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.VEHICLE_TYPE_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
		if (request.getStock() == null || request.getStock() < 0) {
			throw new ProductException(
	                ErrorCodeEnum.STOCK_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.STOCK_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
		}
		
	}
}
