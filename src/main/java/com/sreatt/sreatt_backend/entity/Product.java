package com.sreatt.sreatt_backend.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable = false, length = 100)
    private String name;
	
    private String description;
    
    @Column(nullable = false)
    private double price;
    @Column(nullable = false, unique = true)
    private String serialNo;
    
    @Column(nullable = false)
    private Integer stock;
  
    
    @ElementCollection
    @CollectionTable(
        name = "product_image",
        joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    private List<String> images;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_vehicle_type",
        joinColumns = @JoinColumn(name = "product_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "vehicle_type_id", nullable = false)
    )
    private List<VehicleType> vehicleTypes;
    
    private double voltage;
//    private BigDecimal voltage;
    private Integer cca;
    private Integer ampHours;
    private Integer reserveCapacity;
    private Integer warrantyMonths;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battery_chemistry_id", nullable = false)
    private BatteryChemistry batteryChemistry;

}