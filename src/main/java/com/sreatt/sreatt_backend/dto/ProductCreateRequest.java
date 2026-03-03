package com.sreatt.sreatt_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductCreateRequest {

    private String name;

    private String description;

    private Double price;

    private String serialNo;
    
    private Integer stock;

    private List<Long> vehicleTypeIds;

    private Double voltage;
    private Integer cca;
    private Integer ampHours;
    private Integer reserveCapacity;
    private Integer warrantyMonths;

    private Long brandId;

    private Long batteryChemistryId;
    private List<String> existingImages;
}
