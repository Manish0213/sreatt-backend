package com.sreatt.sreatt_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sreatt.sreatt_backend.entity.Product;
import com.sreatt.sreatt_backend.entity.Warranty;
import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;

public interface WarrantyRepository extends JpaRepository<Warranty, Long> {

    List<Warranty> findByStatus(WarrantyStatus status);
    
    boolean existsBySerialNo(String serialNo);
    
//    Page<Warranty> findByProduct_NameContainingIgnoreCaseAndStatus(
//            String name,
//            WarrantyStatus status,
//            Pageable pageable
//    );
//
//    Page<Warranty> findByProduct_NameContainingIgnoreCase(
//            String name,
//            Pageable pageable
//    );
    
    Page<Warranty> findByUser_NameContainingIgnoreCaseAndStatus(
            String name,
            WarrantyStatus status,
            Pageable pageable
    );

    Page<Warranty> findByUser_NameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Warranty> findByStatus(
            WarrantyStatus status,
            Pageable pageable
    );
    
    Optional<Warranty> findByProductSerialNo(String serialNo);
    
    boolean existsByProduct(Product product);
    
    Optional<Warranty> findByUser_IdAndProduct_SerialNo(Long userId, String serialNo);
}
