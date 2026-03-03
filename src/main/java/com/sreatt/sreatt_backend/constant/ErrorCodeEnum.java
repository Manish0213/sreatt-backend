package com.sreatt.sreatt_backend.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	GENERIC_ERROR("20000", "Something went wrong. Please try again later."),
	RESOURCE_NOT_FOUND("20001", "Invalid URL. Please check and try again."),
	USER_ALREADY_EXISTS("20002", "User with given details already exists."),
	DEALER_CODE_EXISTS("20003", "Dealer code already exists. Please use a different dealer code."),
	ALREADY_DISTRIBUTOR_REQUESTED("20004", "Distributor request already submitted."),
	JWT_EXPIRED("20005", "JWT token has expired. Please login again."),
	IMAGE_UPLOAD_FAILED("PRD_5001", "Failed to upload product image"),
	BRAND_NOT_FOUND("PRD_4005", "Brand ID is required."),
	INVALID_VEHICLE_TYPE("PRD_4001", "Invalid vehicle types selected"),
	BATTERY_CHEMISTRY_NOT_FOUND("PRD_4006", "Battery chemistry not found"),
	DUPLICATE_SERIAL_NUMBER("PRD_4093", "Serial number already exists"),
	PRODUCT_NOT_FOUND("PRD_4004", "Product not found"),
	NO_PRODUCT_NOT_FOUND("PRD_4010", "No Product found"),
	
	// ========= VALIDATION ERRORS (400) =========
	INVALID_REQUEST_DATA("PRD_4000", "Invalid request data"),
	VEHICLE_TYPE_REQUIRED("PRD_4001", "At least one vehicle type is required"),
	INVALID_PRODUCT_NAME("PRD_4002", "Product name is required and cannot be empty"),
	INVALID_PRODUCT_PRICE("PRD_4003", "Product price must be greater than zero"),
	INVALID_SERIAL_NUMBER("PRD_4008", "Serial number is required"),
	BRAND_ID_REQUIRED("PRD_4009", "Brand ID is required"),
	BATTERY_CHEMISTRY_ID_REQUIRED("PRD_4011", "Battery chemistry ID is required"),
	STOCK_REQUIRED("PRD_4013", "Stock quantity is required");

	
	private final String errorCode;
	private final String errorMessage;
	
	ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}