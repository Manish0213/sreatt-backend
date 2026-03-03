package com.sreatt.sreatt_backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sreatt.sreatt_backend.dto.WarrantyListResponseDto;
import com.sreatt.sreatt_backend.dto.WarrantyRequest;
import com.sreatt.sreatt_backend.dto.WarrantyRequestDto;
import com.sreatt.sreatt_backend.dto.WarrantyResponse;
import com.sreatt.sreatt_backend.dto.WarrantyResponseDto;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;
import com.sreatt.sreatt_backend.service.impl.WarrantyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/warranty")
@RequiredArgsConstructor
public class WarrantyController {

    private final WarrantyService warrantyService;
    
    @GetMapping("/warranties")
    public ResponseEntity<Page<WarrantyListResponseDto>> getWarranties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) WarrantyStatus status
    ) {

        return ResponseEntity.ok(
                warrantyService.getWarranties(page, size, search, status)
        );
    }
    
    @GetMapping
    public ResponseEntity<List<WarrantyListResponseDto>> getAllWarranties() {

        List<WarrantyListResponseDto> warranties = warrantyService.getAllWarranties();

        return ResponseEntity.ok(warranties);
    }

    // User warranty register request
    @PostMapping("/register")
    public ResponseEntity<?> registerWarranty(
            @AuthenticationPrincipal User user,
            @RequestBody WarrantyRequestDto dto) {

    	WarrantyResponseDto response = warrantyService.registerWarranty(user, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    
 // ✅ APPROVE warranty (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{warrantyId}/approve")
    public ResponseEntity<WarrantyResponseDto> approveWarranty(@PathVariable Long warrantyId) {
        WarrantyResponseDto response = warrantyService.approveWarranty(warrantyId);
        return ResponseEntity.ok(response);
    }
    
    // ❌ REJECT warranty (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{warrantyId}/reject")
    public ResponseEntity<WarrantyResponseDto> rejectWarranty(@PathVariable Long warrantyId) {
    	WarrantyResponseDto response = warrantyService.rejectWarranty(warrantyId);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{warrantyId}")
    public ResponseEntity<WarrantyResponseDto> updateWarrantyStatus(
            @PathVariable Long warrantyId,
            @RequestParam WarrantyStatus status) {

        WarrantyResponseDto response = warrantyService.updateWarrantyStatus(warrantyId, status);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/check")
    public ResponseEntity<?> checkWarranty(
            @RequestBody WarrantyRequest request) {

        WarrantyResponse response = warrantyService.checkWarranty(
                request.getSerialNo()
//                request.getPurchaseDate()
        );

        return ResponseEntity.ok(response);
    }
}
