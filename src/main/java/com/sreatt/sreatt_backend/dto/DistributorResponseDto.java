package com.sreatt.sreatt_backend.dto;

import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributorResponseDto {
	private Long id;
    private DistributorStatus status;
}