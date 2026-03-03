package com.sreatt.sreatt_backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sreatt.sreatt_backend.entity.Distributor;
import com.sreatt.sreatt_backend.entity.Product;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {
    boolean existsByUser(User user);
    boolean existsByDealerCode(String dealerCode);
    
    Page<Distributor> findByStatus(
            DistributorStatus status,
            Pageable pageable
    );

    Page<Distributor> findByShopNameContainingIgnoreCase(
            String shopName,
            Pageable pageable
    );

    Page<Distributor> findByShopNameContainingIgnoreCaseAndStatus(
            String shopName,
            DistributorStatus status,
            Pageable pageable
    );
    
    Page<Distributor> findByUser_NameContainingIgnoreCaseAndStatus(
            String name,
            DistributorStatus status,
            Pageable pageable
    );
    
    Page<Distributor> findByUser_NameContainingIgnoreCase(
            String name,
            Pageable pageable
    );
	
    Optional<Distributor> findByDealerCode(String dealerCode);
    
    
	
}