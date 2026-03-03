package com.sreatt.sreatt_backend.dto;

import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyResponseDto {
	private Long id;
	private WarrantyStatus status;
}