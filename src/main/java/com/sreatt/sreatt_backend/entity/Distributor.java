package com.sreatt.sreatt_backend.entity;

import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Distributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String dealerCode; 
    
    private String shopName;
    
    @Embedded
    private Address address;

//    @ElementCollection   never use it on a single lever field
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistributorStatus status = DistributorStatus.PENDING;
}
