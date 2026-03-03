package com.sreatt.sreatt_backend.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sreatt.sreatt_backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	Page<Product> findAll(Pageable pageable);
	boolean existsBySerialNo(String serialNo);
	
	Optional<Product> findBySerialNo(String serialNo);
//	List<Product> findByVehicleTypes_Id(Long vehicleTypeId);
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
	Page<Product> findByVehicleTypes_Id(Long vehicleTypeId, Pageable pageable);
}