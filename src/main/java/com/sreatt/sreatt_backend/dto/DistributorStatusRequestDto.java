package com.sreatt.sreatt_backend.dto;

import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;

import lombok.Data;

@Data
public class DistributorStatusRequestDto {

	private DistributorStatus status;
}