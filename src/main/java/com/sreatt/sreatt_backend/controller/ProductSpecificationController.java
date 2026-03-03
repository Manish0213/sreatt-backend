package com.sreatt.sreatt_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sreatt.sreatt_backend.dto.BatteryChemistryDto;
import com.sreatt.sreatt_backend.dto.BrandDto;
import com.sreatt.sreatt_backend.dto.VehicleTypeDto;
import com.sreatt.sreatt_backend.entity.BatteryChemistry;
import com.sreatt.sreatt_backend.entity.Brand;
import com.sreatt.sreatt_backend.entity.VehicleType;
import com.sreatt.sreatt_backend.repository.BatteryChemistryRepository;
import com.sreatt.sreatt_backend.repository.BrandRepository;
import com.sreatt.sreatt_backend.repository.VehicleTypeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/specifications")
public class ProductSpecificationController {
	private final VehicleTypeRepository vehicleTypeRepository;
	private final BrandRepository brandRepository;
	private final BatteryChemistryRepository batteryChemistryRepository;

	@GetMapping("/vehicle-types")
	public List<VehicleTypeDto> getAllVehicleTypes() {
		
		List<VehicleType> response = vehicleTypeRepository.findAll();
		return response.stream()
				.map(vehicleType -> new VehicleTypeDto(
						vehicleType.getId(),
						vehicleType.getName()
				))
				.toList();
	}
	
	@GetMapping("/brands")
	public List<BrandDto> getAllBrands() {
		List<Brand> response = brandRepository.findAll();
		return response.stream()
				.map(brand -> new BrandDto(
						brand.getId(),
						brand.getName()
				))
				.toList();
	}
	
	@GetMapping("/battery-chemistries")
	public List<BatteryChemistryDto> getAllBatteryChemistries() {
		List<BatteryChemistry> response = batteryChemistryRepository.findAll();
		return response.stream()
				.map(batteryChemistry -> new BatteryChemistryDto(
						batteryChemistry.getId(),
						batteryChemistry.getName()
				))
				.toList();
	}
}
