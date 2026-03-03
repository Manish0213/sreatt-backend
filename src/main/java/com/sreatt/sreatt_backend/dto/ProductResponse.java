package com.sreatt.sreatt_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponse {

	private String serialNo;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    // Related data as names only
//    private String brandName;
    private BrandDto brandDto;
//    private String batteryChemistryName;
    private BatteryChemistryDto batteryChemistryDto;
//    private List<String> vehicleTypeNames;
    private List<VehicleTypeDto> vehicleTypesDto;

    // Battery specs
    private Double voltage;
    private Integer cca;
    private Integer ampHours;
    private Integer reserveCapacity;
    private Integer warrantyMonths;

    private List<String> images;
}
