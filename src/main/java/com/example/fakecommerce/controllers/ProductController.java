package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateProductRequestDTO;
import com.example.fakecommerce.dtos.GetProductResponseDTO;
import com.example.fakecommerce.dtos.GetProductWithDetailsResponseDTO;
import com.example.fakecommerce.schema.Product;
import com.example.fakecommerce.services.ProductService;
import com.example.fakecommerce.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetProductResponseDTO>>> getAllProducts() {
        log.info("REST request to get all products");
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts(), "Products retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetProductResponseDTO>> getProductById(@PathVariable Long id) {
        log.info("REST request to get product with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id), "Product retrieved successfully"));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<GetProductWithDetailsResponseDTO>> getProductDetailsById(@PathVariable Long id) {
        log.info("REST request to get product details with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(productService.getProductWithDetailsById(id), "Product details retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody CreateProductRequestDTO requestDTO) {
        log.info("REST request to create product: {}", requestDTO != null ? requestDTO.getTitle() : null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productService.createProduct(requestDTO), "Product created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@RequestParam("categoryName") String category) {
        log.info("REST request to search products by category: {}", category);
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(category),
                "Products retrieved successfully"));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllUniqueCategories() {
        log.info("REST request to get all unique categories");
        return ResponseEntity.ok(ApiResponse.success(productService.getAllUniqueCategories(),
                "Categories retrieved successfully"));
    }
}
