package com.sreatt.sreatt_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BatteryChemistry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 100, unique = true)
    private String name;
	
	// 🔥 Reverse mapping (optional but recommended)
    @OneToMany(mappedBy = "batteryChemistry", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
}
