package com.sreatt.sreatt_backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WarrantyRequestDto {
    private String dealerCode;
    private LocalDate purchaseDate;
    private String serialNo;
    private AddressDto address;
}