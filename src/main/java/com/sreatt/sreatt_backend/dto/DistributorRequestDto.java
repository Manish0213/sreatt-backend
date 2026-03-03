package com.sreatt.sreatt_backend.dto;

import lombok.Data;

@Data
public class DistributorRequestDto {
	private String dealerCode;
    private String shopName;
    private AddressDto address;
}