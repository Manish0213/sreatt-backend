package com.sreatt.sreatt_backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Warranty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String dealerCode;

    private LocalDate purchaseDate;

	@Column(unique = true)
	private String serialNo;
    
    @Embedded
    private Address address;

//    @ElementCollection
    @Enumerated(EnumType.STRING)
    private WarrantyStatus status = WarrantyStatus.PENDING;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributor_id", nullable = false)
    private Distributor distributor;

    private LocalDateTime createdAt = LocalDateTime.now();
}
