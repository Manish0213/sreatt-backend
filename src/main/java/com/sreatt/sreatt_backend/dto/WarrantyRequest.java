package com.sreatt.sreatt_backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WarrantyRequest {
	private String serialNo;
    private LocalDate purchaseDate;
}
