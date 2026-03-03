package com.sreatt.sreatt_backend.dto;

import java.time.LocalDate;

import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyResponse {
	private String serialNo;
    private LocalDate purchaseDate;
    private Integer warrantyMonths;
    private LocalDate expiryDate;
    private String status;
    private long remainingDays;
    private WarrantyStatus warrantyStatus;
}