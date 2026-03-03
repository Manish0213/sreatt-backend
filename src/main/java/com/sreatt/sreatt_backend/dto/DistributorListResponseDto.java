package com.sreatt.sreatt_backend.dto;

import lombok.Data;

@Data
public class DistributorListResponseDto {

    private Long id;
    private String dealerCode;
    private String shopName;

    private String city;
    private String state;
    private String area;

    private String status;
    
 // Optional (agar user details bhi dikhani ho)
    private String dealerName;
    private String email;
}