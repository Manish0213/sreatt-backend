package com.sreatt.sreatt_backend.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sreatt.sreatt_backend.constant.ErrorCodeEnum;
import com.sreatt.sreatt_backend.dto.DistributorListResponseDto;
import com.sreatt.sreatt_backend.dto.DistributorRequestDto;
import com.sreatt.sreatt_backend.dto.DistributorResponseDto;
import com.sreatt.sreatt_backend.entity.Address;
import com.sreatt.sreatt_backend.entity.Distributor;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.enums.DistributorStatus;
import com.sreatt.sreatt_backend.entity.enums.Role;
import com.sreatt.sreatt_backend.exceptions.DistributorException;
import com.sreatt.sreatt_backend.repository.DistributorRepository;
import com.sreatt.sreatt_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributorService {
	private final DistributorRepository distributorRepository;
    private final UserRepository userRepository;
    
    public DistributorResponseDto requestForDistributor(String email, DistributorRequestDto dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Already distributor request exists
        if (distributorRepository.existsByUser(user)) {
        	
            throw new DistributorException(
                    ErrorCodeEnum.ALREADY_DISTRIBUTOR_REQUESTED.getErrorCode(),
                    ErrorCodeEnum.ALREADY_DISTRIBUTOR_REQUESTED.getErrorMessage(),
//                    HttpStatus.BAD_REQUEST
                    HttpStatus.CONFLICT
                );
        }

        // Dealer code duplicate check
        if (distributorRepository.existsByDealerCode(dto.getDealerCode())) {
            
            throw new DistributorException(
					ErrorCodeEnum.DEALER_CODE_EXISTS.getErrorCode(),
					ErrorCodeEnum.DEALER_CODE_EXISTS.getErrorMessage(),
					HttpStatus.BAD_REQUEST
					);
        }
        
        Address address = new Address();
        address.setCity(dto.getAddress().getCity());
        address.setState(dto.getAddress().getState());
        address.setArea(dto.getAddress().getArea());
        
        Distributor distributor = new Distributor();
        distributor.setUser(user);
        distributor.setDealerCode(dto.getDealerCode());
        distributor.setShopName(dto.getShopName());
        distributor.setAddress(address);
        distributor.setStatus(DistributorStatus.PENDING);

        Distributor savedDistributor = distributorRepository.save(distributor);
        
     // ✅ DTO return karo
        return new DistributorResponseDto(
                savedDistributor.getId(),
                savedDistributor.getStatus()
        );
    }
    
//    @Override
//    public List<DistributorResponseDto> getAllDistributors() {
//        return distributorRepository.findAll()
//                .stream()
//                .map(d -> modelMapper.map(d, DistributorResponseDto.class))
//                .toList();
//    }
    
    @Transactional
    public DistributorResponseDto updateDistributorStatus(Long distributorId, DistributorStatus status) {

        Distributor distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));

        if (distributor.getStatus() == DistributorStatus.APPROVED) {
            throw new RuntimeException("Distributor already approved");
        }

        distributor.setStatus(status);

        if (status == DistributorStatus.APPROVED) {
            User user = distributor.getUser();
            user.setRole(Role.DISTRIBUTER);
            userRepository.save(user);
        }

        distributorRepository.save(distributor);
        
     // 👇 DTO return karo
        return new DistributorResponseDto(
                distributor.getId(),
                distributor.getStatus()
        );
        
    }

	public List<DistributorListResponseDto> getAllDistributors() {
		return distributorRepository.findAll()
	            .stream()
//	            .filter(d -> "APPROVED".equals(d.getStatus()))
	            .filter(d -> d.getStatus() == DistributorStatus.APPROVED) // ✅ sirf APPROVED
	            .map(this::mapToDto)
	            .toList();
	}
    
    public Page<DistributorListResponseDto> getDistributors(
            int page,
            int size,
            String search,
            DistributorStatus status
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Distributor> distributorPage;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasStatus = status != null;

        // 🔥 Case 1: Search + Status
        if (hasSearch && hasStatus) {
//            distributorPage = distributorRepository
//                    .findByShopNameContainingIgnoreCaseAndStatus(
//                            search.trim(),
//                            status,
//                            pageable
//                    );
        	
        	distributorPage = distributorRepository
                    .findByUser_NameContainingIgnoreCaseAndStatus(
                            search.trim(),
                            status,
                            pageable
                    );
        }

        // 🔥 Case 2: Only Search
//        else if (hasSearch) {
//            distributorPage = distributorRepository
//                    .findByShopNameContainingIgnoreCase(
//                            search.trim(),
//                            pageable
//                    );
//        }
        
        else if (hasSearch) {
            distributorPage = distributorRepository
                    .findByUser_NameContainingIgnoreCase(
                            search.trim(),
                            pageable
                    );
        }

        // 🔥 Case 3: Only Status
        else if (hasStatus) {
            distributorPage = distributorRepository
                    .findByStatus(status, pageable);
        }

        // 🔥 Case 4: Normal Pagination
        else {
            distributorPage = distributorRepository
                    .findAll(pageable);
        }

        // 🔥 Convert Entity → DTO
        return distributorPage.map(this::mapToDto);
    }
	
	private DistributorListResponseDto mapToDto(Distributor distributor) {

	    DistributorListResponseDto dto = new DistributorListResponseDto();

	    dto.setId(distributor.getId());
	    dto.setDealerCode(distributor.getDealerCode());
	    dto.setShopName(distributor.getShopName());

	    // Address (Embedded)
	    if (distributor.getAddress() != null) {
	        dto.setCity(distributor.getAddress().getCity());
	        dto.setState(distributor.getAddress().getState());
	        dto.setArea(distributor.getAddress().getArea());
	    }

	    dto.setStatus(distributor.getStatus().name());
	    
	 // User Details (optional)
	    if (distributor.getUser() != null) {
	        dto.setDealerName(distributor.getUser().getName());
	        dto.setEmail(distributor.getUser().getEmail());
	    }

	    return dto;
	}

}
