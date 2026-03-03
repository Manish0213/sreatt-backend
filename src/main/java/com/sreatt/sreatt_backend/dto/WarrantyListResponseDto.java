package com.sreatt.sreatt_backend.dto;

import java.time.LocalDate;

import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;

import lombok.Data;

@Data
public class WarrantyListResponseDto {

	private Long id;

    private String productName;
    private String productImage;
    private String productSerialNo;

    private String userName;
    private String userEmail;

    private String distributorName;
    private String distributorShopName;
    private String distributorDealerCode;
    
    private WarrantyStatus status;
    private LocalDate purchaseDate;
    
}
