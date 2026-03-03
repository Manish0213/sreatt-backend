package com.sreatt.sreatt_backend.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sreatt.sreatt_backend.constant.ErrorCodeEnum;
import com.sreatt.sreatt_backend.dto.BatteryChemistryDto;
import com.sreatt.sreatt_backend.dto.BrandDto;
import com.sreatt.sreatt_backend.dto.ProductCreateRequest;
import com.sreatt.sreatt_backend.dto.ProductCreateResponse;
import com.sreatt.sreatt_backend.dto.ProductResponse;
import com.sreatt.sreatt_backend.dto.VehicleTypeDto;
import com.sreatt.sreatt_backend.entity.BatteryChemistry;
import com.sreatt.sreatt_backend.entity.Brand;
import com.sreatt.sreatt_backend.entity.Product;
import com.sreatt.sreatt_backend.entity.VehicleType;
import com.sreatt.sreatt_backend.exceptions.ProductException;
import com.sreatt.sreatt_backend.repository.BatteryChemistryRepository;
import com.sreatt.sreatt_backend.repository.BrandRepository;
import com.sreatt.sreatt_backend.repository.ProductRepository;
import com.sreatt.sreatt_backend.repository.VehicleTypeRepository;
import com.sreatt.sreatt_backend.repository.WarrantyRepository;
import com.sreatt.sreatt_backend.service.RequestValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl {
	
    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final BrandRepository brandRepository;
    private final BatteryChemistryRepository batteryChemistryRepository;
    private final RequestValidator requestValidator;
    private final ModelMapper modelMapper;
    private final WarrantyRepository warrantyRepository;

    public ProductCreateResponse saveProduct(
            ProductCreateRequest createRequest,
            MultipartFile[] images
            
    ) {
    	
    	requestValidator.validateProductCreateRequest(createRequest);
    	log.info("Create product request validated successfully");
    	
    	if (productRepository.existsBySerialNo(createRequest.getSerialNo())) {
    	    throw new ProductException(
    	            ErrorCodeEnum.DUPLICATE_SERIAL_NUMBER.getErrorCode(),
    	            ErrorCodeEnum.DUPLICATE_SERIAL_NUMBER.getErrorMessage(),
    	            HttpStatus.CONFLICT
    	    );
    	}
    	
    	List<VehicleType> vehicleTypes = new ArrayList<>();
    	
    	if (createRequest.getVehicleTypeIds() != null && !createRequest.getVehicleTypeIds().isEmpty()) {

    		 vehicleTypes = vehicleTypeRepository.findAllById(createRequest.getVehicleTypeIds());

    	    if (vehicleTypes.size() != createRequest.getVehicleTypeIds().size()) {
    	        throw new ProductException(
    	                ErrorCodeEnum.INVALID_VEHICLE_TYPE.getErrorCode(),
    	                ErrorCodeEnum.INVALID_VEHICLE_TYPE.getErrorMessage(),
    	                HttpStatus.NOT_FOUND
    	        );
    	    }
    	}
        
    	List<String> imageUrls = new ArrayList<>();

    	if (images != null && images.length > 0) {
    		log.info("image length: {} ", images.length);    // length 1 aa rhi h

    	    for (MultipartFile file : images) {
    	    	log.info("File name: {}, size: {}", file.getOriginalFilename(), file.getSize());
    	    	
    	    	if (file == null || file.isEmpty()) {
    	            continue;   // skip empty file
    	        }
    	    	
    	        try {
    	            Map uploadResult = cloudinary.uploader().upload(
    	                    file.getBytes(),
    	                    ObjectUtils.asMap("folder", "sreatt")
    	            );

    	            imageUrls.add(uploadResult.get("secure_url").toString());

    	        } catch (IOException e) {
    	            throw new ProductException(
    	                    ErrorCodeEnum.IMAGE_UPLOAD_FAILED.getErrorCode(),
    	                    ErrorCodeEnum.IMAGE_UPLOAD_FAILED.getErrorMessage(),
    	                    HttpStatus.INTERNAL_SERVER_ERROR
    	            );
    	        }
    	    }
    	}


     // Fetch Brand
        Brand brand = brandRepository.findById(createRequest.getBrandId())
				.orElseThrow(() -> new ProductException(
						ErrorCodeEnum.BRAND_NOT_FOUND.getErrorCode(),
						ErrorCodeEnum.BRAND_NOT_FOUND.getErrorMessage(),
						HttpStatus.NOT_FOUND
						));
        
     // Fetch BatteryChemistry
        BatteryChemistry batteryChemistry = batteryChemistryRepository.findById(createRequest.getBatteryChemistryId())
        						.orElseThrow(() -> new ProductException(
        								ErrorCodeEnum.BATTERY_CHEMISTRY_NOT_FOUND.getErrorCode(),
										ErrorCodeEnum.BATTERY_CHEMISTRY_NOT_FOUND.getErrorMessage(),
										HttpStatus.NOT_FOUND
										));

        Product product = new Product();
        product.setName(createRequest.getName());
        product.setDescription(createRequest.getDescription());
        product.setPrice(createRequest.getPrice());
        product.setSerialNo(createRequest.getSerialNo());
        product.setVehicleTypes(vehicleTypes);
        product.setImages(imageUrls);
        product.setVoltage(createRequest.getVoltage() != null ? createRequest.getVoltage() : 0.0);
        product.setCca(createRequest.getCca());
        product.setAmpHours(createRequest.getAmpHours());
        product.setReserveCapacity(createRequest.getReserveCapacity());
        product.setWarrantyMonths(createRequest.getWarrantyMonths());
        product.setBrand(brand);
        product.setBatteryChemistry(batteryChemistry);
        product.setStock(createRequest.getStock());

        Product savedProduct = productRepository.save(product);
        ProductCreateResponse response = modelMapper.map(savedProduct, ProductCreateResponse.class);
        
        return response;
    }
    
    public ProductCreateResponse updateProduct(
//            Long id,
    		String serialNo,
            ProductCreateRequest updateRequest,
            MultipartFile[] images
    ) {

//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ProductException(
//                        ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
//                        ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
//                        HttpStatus.NOT_FOUND
//                ));
    	
    	Product product = productRepository.findBySerialNo(serialNo)
                .orElseThrow(() -> new ProductException(
                        ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        requestValidator.validateProductCreateRequest(updateRequest);

        // 🔥 Duplicate Serial Check (exclude current product)
        if (!product.getSerialNo().equals(updateRequest.getSerialNo()) &&
                productRepository.existsBySerialNo(updateRequest.getSerialNo())) {

            throw new ProductException(
                    ErrorCodeEnum.DUPLICATE_SERIAL_NUMBER.getErrorCode(),
                    ErrorCodeEnum.DUPLICATE_SERIAL_NUMBER.getErrorMessage(),
                    HttpStatus.CONFLICT
            );
        }

        // 🔥 VehicleTypes Update
        List<VehicleType> vehicleTypes = new ArrayList<>();

        if (updateRequest.getVehicleTypeIds() != null &&
                !updateRequest.getVehicleTypeIds().isEmpty()) {

            vehicleTypes = vehicleTypeRepository
                    .findAllById(updateRequest.getVehicleTypeIds());

            if (vehicleTypes.size() != updateRequest.getVehicleTypeIds().size()) {
                throw new ProductException(
                        ErrorCodeEnum.INVALID_VEHICLE_TYPE.getErrorCode(),
                        ErrorCodeEnum.INVALID_VEHICLE_TYPE.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                );
            }
        }

        // 🔥 Image Update Logic
//        List<String> imageUrls = product.getImages(); // existing images

//        if (images != null && images.length > 0) {
//
//            imageUrls = new ArrayList<>();
//
//            for (MultipartFile file : images) {
//                try {
//                    Map uploadResult = cloudinary.uploader().upload(
//                            file.getBytes(),
//                            ObjectUtils.asMap("folder", "sreatt")
//                    );
//
//                    imageUrls.add(uploadResult.get("secure_url").toString());
//
//                } catch (IOException e) {
//                    throw new ProductException(
//                            ErrorCodeEnum.IMAGE_UPLOAD_FAILED.getErrorCode(),
//                            ErrorCodeEnum.IMAGE_UPLOAD_FAILED.getErrorMessage(),
//                            HttpStatus.INTERNAL_SERVER_ERROR
//                    );
//                }
//            }
//        }

        // 🔥 Step 1: Final image list banayenge
        List<String> finalImages = new ArrayList<>();
        
        // ================================
        // 1️ Retain Existing Images
        // ================================
        
        if (updateRequest.getExistingImages() != null && 
                !updateRequest.getExistingImages().isEmpty()) {

                finalImages.addAll(updateRequest.getExistingImages());
            }
        
        // ================================
        // 2️ Upload New Images (if any)
        // ================================
        if (images != null && images.length > 0) {

            for (MultipartFile file : images) {

                if (file == null || file.isEmpty()) {
                    continue;   // skip empty file
                }

                try {

                    Map uploadResult = cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap("folder", "sreatt")
                    );

                    String imageUrl = uploadResult.get("secure_url").toString();
                    finalImages.add(imageUrl);

                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed");
                }
            }
        }
        
        // ================================
        // 3️ Set Final Images
        // ================================
        product.setImages(finalImages);
        
        // 🔥 Fetch Brand
        Brand brand = brandRepository.findById(updateRequest.getBrandId())
                .orElseThrow(() -> new ProductException(
                        ErrorCodeEnum.BRAND_NOT_FOUND.getErrorCode(),
                        ErrorCodeEnum.BRAND_NOT_FOUND.getErrorMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // 🔥 Fetch BatteryChemistry
        BatteryChemistry batteryChemistry =
                batteryChemistryRepository.findById(updateRequest.getBatteryChemistryId())
                        .orElseThrow(() -> new ProductException(
                                ErrorCodeEnum.BATTERY_CHEMISTRY_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.BATTERY_CHEMISTRY_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        ));

        // 🔥 Set Updated Fields
        product.setName(updateRequest.getName());
        product.setDescription(updateRequest.getDescription());
        product.setPrice(updateRequest.getPrice());
        product.setSerialNo(updateRequest.getSerialNo());
        product.setVehicleTypes(vehicleTypes);
//        product.setImages(imageUrls);
        product.setVoltage(updateRequest.getVoltage());
        product.setCca(updateRequest.getCca());
        product.setAmpHours(updateRequest.getAmpHours());
        product.setReserveCapacity(updateRequest.getReserveCapacity());
        product.setWarrantyMonths(updateRequest.getWarrantyMonths());
        product.setBrand(brand);
        product.setBatteryChemistry(batteryChemistry);
        product.setStock(updateRequest.getStock());

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductCreateResponse.class);
    }
    
    public Page<ProductResponse> getAllProducts(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage;

        if (search != null && !search.trim().isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::mapToResponse);
    }
    
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Product not found with id: " + productId
                        )
                );
    }
    
    public ProductResponse getProductBySerialNo(String serialNo) {

        Product product = productRepository.findBySerialNo(serialNo)
                .orElseThrow(() ->
                        new ProductException(
                                ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(),
                                ErrorCodeEnum.PRODUCT_NOT_FOUND.getErrorMessage(),
                                HttpStatus.NOT_FOUND
                        )
                );

        return mapToResponse(product);
    }
    
    public Page<ProductResponse> getProductsByVehicleType(
            Long vehicleTypeId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage =
                productRepository.findByVehicleTypes_Id(vehicleTypeId, pageable);

        return productPage.map(this::mapToResponse);
    }
    
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setSerialNo(product.getSerialNo());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setBrandDto(modelMapper.map(product.getBrand(), BrandDto.class));
        response.setBatteryChemistryDto(
				modelMapper.map(product.getBatteryChemistry(), BatteryChemistryDto.class)
		);
        response.setVehicleTypesDto(
				product.getVehicleTypes()
						.stream()
						.map(vt -> modelMapper.map(vt, VehicleTypeDto.class))
						.toList()
		);
        response.setVoltage(product.getVoltage());
        response.setCca(product.getCca());
        response.setAmpHours(product.getAmpHours());
        response.setReserveCapacity(product.getReserveCapacity());
        response.setWarrantyMonths(product.getWarrantyMonths());
        
//        log images
        log.info("Product ID: {}, Images: {}", product.getId(), product.getImages());

        response.setImages(product.getImages());

        return response;
    }
    
    public Page<ProductResponse> getFilteredProducts(
            List<Long> vehicleTypes,
            Double minCapacity,
            Double maxCapacity,
            int page,
			int size) {

        Specification<Product> spec = null;

        if (vehicleTypes != null && !vehicleTypes.isEmpty()) {
//            spec = (root, query, cb) ->
//                    root.get("vehicleTypes").get("id").in(vehicleTypes);
        	Specification<Product> vehicleSpec = (root, query, cb) -> {
                query.distinct(true);  // ✅ remove duplicates
                return root.join("vehicleTypes").get("id").in(vehicleTypes);
            };

            spec = vehicleSpec;
        }

        if (minCapacity != null && maxCapacity != null) {
            Specification<Product> capacitySpec =
                    (root, query, cb) ->
                            cb.between(root.get("ampHours"), minCapacity, maxCapacity);

            spec = (spec == null) ? capacitySpec : spec.and(capacitySpec);
        }

        if (minCapacity != null && maxCapacity == null) {
            Specification<Product> capacitySpec =
                    (root, query, cb) ->
                            cb.greaterThanOrEqualTo(root.get("ampHours"), minCapacity);

            spec = (spec == null) ? capacitySpec : spec.and(capacitySpec);
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Product> productPage = (spec == null) ? productRepository.findAll(pageable)
                              : productRepository.findAll(spec, pageable);
        
        return productPage.map(this::mapToResponse);
    }
    
    public void deleteProductBySerialNo(String serialNo) {

        Product product = productRepository.findBySerialNo(serialNo)
        		.orElseThrow(() -> new ProductException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found with serialNo: " + serialNo,
                        HttpStatus.NOT_FOUND
                ));
        
     // 🔥 Check if product exists in warranty table
        boolean isUsedInWarranty = warrantyRepository.existsByProduct(product);

        if (isUsedInWarranty) {
            throw new ProductException(
                    "PRODUCT_IN_USE",
                    "This product cannot be deleted because it is associated with warranty records.",
                    HttpStatus.BAD_REQUEST
            );
        }

        productRepository.delete(product);
    }
}