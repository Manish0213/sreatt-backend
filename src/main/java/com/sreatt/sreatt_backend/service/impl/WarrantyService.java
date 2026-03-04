package com.sreatt.sreatt_backend.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sreatt.sreatt_backend.dto.WarrantyListResponseDto;
import com.sreatt.sreatt_backend.dto.WarrantyRequestDto;
import com.sreatt.sreatt_backend.dto.WarrantyResponse;
import com.sreatt.sreatt_backend.dto.WarrantyResponseDto;
import com.sreatt.sreatt_backend.entity.Address;
import com.sreatt.sreatt_backend.entity.Distributor;
import com.sreatt.sreatt_backend.entity.Product;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.Warranty;
import com.sreatt.sreatt_backend.entity.enums.WarrantyStatus;
import com.sreatt.sreatt_backend.exceptions.WarrantyException;
import com.sreatt.sreatt_backend.repository.DistributorRepository;
import com.sreatt.sreatt_backend.repository.ProductRepository;
import com.sreatt.sreatt_backend.repository.WarrantyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyService {

	private final WarrantyRepository warrantyRepository;
	private final ProductRepository productRepository;
	private final DistributorRepository distributorRepository;

	public Page<WarrantyListResponseDto> getWarranties(
			int page,
			int size,
			String search,
			WarrantyStatus status
			) {

		Pageable pageable = PageRequest.of(
				page,
				size,
				Sort.by(Sort.Direction.DESC, "id")
				);

		Page<Warranty> warrantyPage;

		boolean hasSearch = search != null && !search.trim().isEmpty();
		boolean hasStatus = status != null;

		// 🔥 Case 1: Search + Status
		if (hasSearch && hasStatus) {
			warrantyPage = warrantyRepository
					.findByUser_NameContainingIgnoreCaseAndStatus(
							search.trim(),
							status,
							pageable
							);
		}

		// 🔥 Case 2: Only Search
		else if (hasSearch) {
			warrantyPage = warrantyRepository
					.findByUser_NameContainingIgnoreCase(
							search.trim(),
							pageable
							);
		}

		// 🔥 Case 3: Only Status
		else if (hasStatus) {
			warrantyPage = warrantyRepository
					.findByStatus(status, pageable);
		}

		// 🔥 Case 4: Normal Pagination
		else {
			warrantyPage = warrantyRepository.findAll(pageable);
		}

		return warrantyPage.map(this::mapToDto);
	}

	private WarrantyListResponseDto mapToDto(Warranty warranty) {

		WarrantyListResponseDto dto = new WarrantyListResponseDto();

		dto.setId(warranty.getId());

		// 🔥 Product Data
		if (warranty.getProduct() != null) {

			dto.setProductName(warranty.getProduct().getName());
			dto.setProductSerialNo(warranty.getProduct().getSerialNo());

			List<String> images = warranty.getProduct().getImages();

			if (images != null && !images.isEmpty()) {
				dto.setProductImage(images.get(0)); // first image
			}
		}

		// 🔥 User Data
		if (warranty.getUser() != null) {
			dto.setUserName(warranty.getUser().getName());
			dto.setUserEmail(warranty.getUser().getEmail());
		}

		// 🔥 Distributor Data
		if (warranty.getDistributor() != null) {
			dto.setDistributorName(warranty.getDistributor().getUser().getName());
			dto.setDistributorShopName(warranty.getDistributor().getShopName());
			dto.setDistributorDealerCode(warranty.getDistributor().getDealerCode());
		}

		// 🔥 Status
		dto.setStatus(warranty.getStatus());

		// 🔥 PurchaseDate	    
		dto.setPurchaseDate(warranty.getPurchaseDate());

		return dto;
	}

	public List<WarrantyListResponseDto> getAllWarranties() {

		List<Warranty> warranties = warrantyRepository.findAll();

		return warranties.stream().map(warranty -> {

			WarrantyListResponseDto dto = new WarrantyListResponseDto();

			dto.setId(warranty.getId());

			// 🔥 Product Data
			dto.setProductName(warranty.getProduct().getName());

			List<String> images = warranty.getProduct().getImages();
			//	        dto.setProductImage(warranty.getProduct().getImageUrl());
			if (images != null && !images.isEmpty()) {
				dto.setProductImage(images.get(0)); // 👈 first image
			}

			// 🔥 User Data
			dto.setUserName(warranty.getUser().getName());
			dto.setUserEmail(warranty.getUser().getEmail());

			//	        Distributor Data
			dto.setDistributorName(warranty.getDistributor().getUser().getName());
			dto.setDistributorShopName(warranty.getDistributor().getShopName());
			dto.setDistributorDealerCode(warranty.getDistributor().getDealerCode());


			dto.setStatus(warranty.getStatus());

			return dto;

		}).toList();
	}


	public WarrantyResponseDto registerWarranty(User user, WarrantyRequestDto dto) {
		log.info("Registering warranty for user: {}, dealerCode: {}, serialNo: {}", user.getId(), dto.getDealerCode(), dto.getSerialNo());

		if (dto.getSerialNo() == null || dto.getSerialNo().isBlank()) {
			throw new WarrantyException(
					"WARRANTY_002",
					"Battery serial number is required",
					HttpStatus.BAD_REQUEST
					);
		}

		// Battery serial duplicate check
		if (warrantyRepository.existsBySerialNo(dto.getSerialNo())) {
			throw new WarrantyException(
					"WARRANTY_001",
					"Warranty already registered for this battery",
					HttpStatus.BAD_REQUEST
					);
		}

		Product product = productRepository.findBySerialNo(dto.getSerialNo())
				.orElseThrow(() -> new WarrantyException(
						"WARRANTY_003",
						"Product with given serial number not found",
						HttpStatus.NOT_FOUND
						));

		Distributor distribuor = distributorRepository.findByDealerCode(dto.getDealerCode())
				.orElseThrow(() -> new WarrantyException(
						"WARRANTY_004",
						"Distributor with given dealer code not found",
						HttpStatus.NOT_FOUND
						));

		Warranty warranty = new Warranty();
		warranty.setUser(user);
		warranty.setDealerCode(dto.getDealerCode());
		warranty.setPurchaseDate(dto.getPurchaseDate());
		warranty.setSerialNo(dto.getSerialNo());  // 👈 updated
		warranty.setStatus(WarrantyStatus.PENDING);
		warranty.setProduct(product);
		warranty.setDistributor(distribuor);

		// 🔥 Address mapping (agar entity me address object hai)
		if (dto.getAddress() != null) {
			Address address = new Address();
			address.setCity(dto.getAddress().getCity());
			address.setState(dto.getAddress().getState());
			address.setArea(dto.getAddress().getArea());

			warranty.setAddress(address);
		}

		Warranty savedWarranty = warrantyRepository.save(warranty);

		// 🔥 Map Entity → Response DTO
		WarrantyResponseDto response = new WarrantyResponseDto();
		response.setId(savedWarranty.getId());
		response.setStatus(savedWarranty.getStatus());

		return response;
	}

	// ✅ APPROVE
	public WarrantyResponseDto approveWarranty(Long warrantyId) {
		Warranty warranty = warrantyRepository.findById(warrantyId)
				.orElseThrow(() -> new WarrantyException(
						"WARRANTY_404",
						"Warranty request not found",
						HttpStatus.NOT_FOUND
						));

		if (warranty.getStatus() != WarrantyStatus.PENDING) {
			throw new WarrantyException(
					"WARRANTY_002",
					"Warranty already processed",
					HttpStatus.BAD_REQUEST
					);
		}

		warranty.setStatus(WarrantyStatus.APPROVED);
		warrantyRepository.save(warranty);

		return new WarrantyResponseDto(warranty.getId(), warranty.getStatus());
	}

	// ❌ REJECT
	public WarrantyResponseDto rejectWarranty(Long warrantyId) {
		Warranty warranty = warrantyRepository.findById(warrantyId)
				.orElseThrow(() -> new WarrantyException(
						"WARRANTY_404",
						"Warranty request not found",
						HttpStatus.NOT_FOUND
						));

		if (warranty.getStatus() != WarrantyStatus.PENDING) {
			throw new WarrantyException(
					"WARRANTY_002",
					"Warranty already processed",
					HttpStatus.BAD_REQUEST
					);
		}

		warranty.setStatus(WarrantyStatus.REJECTED);
		warrantyRepository.save(warranty);

		return new WarrantyResponseDto(warranty.getId(), warranty.getStatus());
	}


	public WarrantyResponseDto updateWarrantyStatus(Long warrantyId, WarrantyStatus status) {

		Warranty warranty = warrantyRepository.findById(warrantyId)
				.orElseThrow(() -> new WarrantyException(
						"WARRANTY_404",
						"Warranty request not found",
						HttpStatus.NOT_FOUND
						));

		if (warranty.getStatus() != WarrantyStatus.PENDING) {
			throw new WarrantyException(
					"WARRANTY_002",
					"Warranty already processed",
					HttpStatus.BAD_REQUEST
					);
		}

		// Safety check: sirf APPROVED ya REJECTED allow karo
		if (status != WarrantyStatus.APPROVED && status != WarrantyStatus.REJECTED) {
			throw new WarrantyException(
					"WARRANTY_003",
					"Invalid status update",
					HttpStatus.BAD_REQUEST
					);
		}

		warranty.setStatus(status);
		warrantyRepository.save(warranty);

		return new WarrantyResponseDto(warranty.getId(), warranty.getStatus());
	}


	public WarrantyResponse checkWarranty(String serialNo) {
		
		// 🔥 Step 1: Current logged-in user id nikalo
	    Long userId = getCurrentUserId();

		Product product = productRepository.findBySerialNo(serialNo)
				.orElseThrow(() -> new WarrantyException(
                        "INVALID_SERIAL",
                        "Invalid Serial Number",
                        HttpStatus.BAD_REQUEST 
                ));
		
		// Find the warranty record for this product
//        Warranty warranty = warrantyRepository.findByProductSerialNo(serialNo)
//                .orElseThrow(() -> new WarrantyException(
//                        "NO_WARRANTY",
//                        "No warranty found for this product",
//                        HttpStatus.BAD_REQUEST
//                ));
		
		Warranty warranty = warrantyRepository
	            .findByUser_IdAndProduct_SerialNo(userId, serialNo)
	            .orElseThrow(() -> new WarrantyException(
	                    "NO_WARRANTY",
	                    "You have not registered warranty for this product",
	                    HttpStatus.BAD_REQUEST
	            ));

        LocalDate purchaseDate = warranty.getPurchaseDate();
		Integer warrantyMonths = product.getWarrantyMonths();

		if (warrantyMonths == null || warrantyMonths == 0) {
			throw new WarrantyException(
                    "NO_WARRANTY",
                    "No warranty available for this product",
                    HttpStatus.BAD_REQUEST
            );
		}

		// Expiry date calculate
		LocalDate expiryDate = purchaseDate.plusMonths(warrantyMonths);
		LocalDate today = LocalDate.now();

		String status;
		long remainingDays;

		if (today.isBefore(expiryDate) || today.isEqual(expiryDate)) {
			status = "ACTIVE";
			remainingDays = ChronoUnit.DAYS.between(today, expiryDate);
		} else {
			status = "EXPIRED";
			remainingDays = 0;
		}

		return new WarrantyResponse(
				serialNo,
				purchaseDate,
				warrantyMonths,
				expiryDate,
				status,
				remainingDays,
				warranty.getStatus()
				);
	}
	
	private Long getCurrentUserId() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User userDetails = (User) authentication.getPrincipal();
	    return userDetails.getId();
	}
}
