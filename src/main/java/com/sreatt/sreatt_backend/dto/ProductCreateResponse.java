package com.sreatt.sreatt_backend.dto;

import lombok.Data;

@Data
public class ProductCreateResponse {
	private String serialNo;
    private String name;
    private Integer stock;
}