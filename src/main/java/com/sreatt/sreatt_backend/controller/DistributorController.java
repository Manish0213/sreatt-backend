package com.sreatt.sreatt_backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sreatt.sreatt_backend.dto.DistributorListResponseDto;
import com.sreatt.sreatt_backend.dto.DistributorRequestDto;
import com.sreatt.sreatt_backend.dto.DistributorResponseDto;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;
import com.sreatt.sreatt_backend.service.impl.DistributorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/distributor")
@RequiredArgsConstructor
public class DistributorController {

	private final DistributorService distributorService;
	
	 @PostMapping("/request")
	    public ResponseEntity<DistributorResponseDto> registerAsDistributor(
	            @AuthenticationPrincipal User user,
	            @RequestBody DistributorRequestDto dto) {

		 DistributorResponseDto response = distributorService.requestForDistributor(user.getEmail(), dto);

		 return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }
	 
	 @GetMapping("/getAllDistributors")
	 public ResponseEntity<List<DistributorListResponseDto>> getAllDistributors() {
	     List<DistributorListResponseDto> distributors = distributorService.getAllDistributors();
	     return ResponseEntity.ok(distributors);
	 }
	 
	 @GetMapping("/distributors")
	 public ResponseEntity<Page<DistributorListResponseDto>> getDistributors(
	         @RequestParam(defaultValue = "0") int page,
	         @RequestParam(defaultValue = "5") int size,
	         @RequestParam(required = false) String search,
	         @RequestParam(required = false) DistributorStatus status
	 ) {
	     return ResponseEntity.ok(
	             distributorService.getDistributors(page, size, search, status)
	     );
	 }
	 
}