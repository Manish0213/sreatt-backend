package com.sreatt.sreatt_backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sreatt.sreatt_backend.dto.ProductCreateRequest;
import com.sreatt.sreatt_backend.dto.ProductCreateResponse;
import com.sreatt.sreatt_backend.dto.ProductResponse;
import com.sreatt.sreatt_backend.entity.Product;
import com.sreatt.sreatt_backend.service.impl.ProductServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
    private final ProductServiceImpl productServiceImpl;
    
 // CREATE PRODUCT
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductCreateResponse> saveProduct(
    		@ModelAttribute ProductCreateRequest createRequest,
            @RequestParam(required = false) MultipartFile[] images
            
    ) {
        return ResponseEntity.ok(
        		productServiceImpl.saveProduct(
        				createRequest, images
                )
        );
    }
   
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{serialNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductCreateResponse> updateProduct(
//            @PathVariable Long id,
    		@PathVariable String serialNo,
            @ModelAttribute ProductCreateRequest updateRequest,
            @RequestParam(required = false) MultipartFile[] images
    ) {
        return ResponseEntity.ok(
                productServiceImpl.updateProduct(serialNo, updateRequest, images)
        );
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String search
    ) {

        Page<ProductResponse> products =
                productServiceImpl.getAllProducts(page, size, search);

        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long productId
    ) {
        Product product = productServiceImpl.getProductById(productId);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/serial/{serialNo}")
    public ResponseEntity<ProductResponse> getProductBySerialNo(
    		@PathVariable String serialNo
    ) {
    	ProductResponse response = productServiceImpl.getProductBySerialNo(serialNo);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/by-vehicle-type/{vehicleTypeId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByVehicleType(
            @PathVariable Long vehicleTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {

        Page<ProductResponse> products =
                productServiceImpl.getProductsByVehicleType(vehicleTypeId, page, size);

        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/filter-products")
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) List<Long> vehicleTypes,
            @RequestParam(required = false) Double minCapacity,
            @RequestParam(required = false) Double maxCapacity,
            @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

        return productServiceImpl.getFilteredProducts(
                vehicleTypes, minCapacity, maxCapacity, page, size);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/serial/{serialNo}")
    public ResponseEntity<String> deleteProductBySerialNo(
            @PathVariable String serialNo
    ) {

        productServiceImpl.deleteProductBySerialNo(serialNo);

        return ResponseEntity.ok("Product deleted successfully");
    }
}