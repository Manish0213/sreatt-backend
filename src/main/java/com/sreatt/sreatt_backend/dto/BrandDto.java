package com.sreatt.sreatt_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {

	private Long id;      // agar expose karna chaho
    private String name;
}