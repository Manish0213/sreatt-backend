package com.sreatt.sreatt_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sreatt.sreatt_backend.dto.DistributorResponseDto;
import com.sreatt.sreatt_backend.dto.DistributorStatusRequestDto;
import com.sreatt.sreatt_backend.service.impl.DistributorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/distributors")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDistributorController {

	private final DistributorService distributorService;
	
//	@GetMapping
//    public ResponseEntity<List<DistributorResponseDto>> getAll() {
//        return ResponseEntity.ok(distributorService.getAllDistributors());
//    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/status")
	public ResponseEntity<DistributorResponseDto> updateStatus(
	        @PathVariable Long id,
	        @RequestBody DistributorStatusRequestDto dto) {

		DistributorResponseDto response = distributorService.updateDistributorStatus(id, dto.getStatus());
	    return ResponseEntity.ok(response);
	}

}
