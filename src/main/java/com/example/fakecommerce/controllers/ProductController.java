package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateProductRequestDTO;
import com.example.fakecommerce.dtos.GetProductResponseDTO;
import com.example.fakecommerce.dtos.GetProductWithDetailsResponseDTO;
import com.example.fakecommerce.schema.Product;
import com.example.fakecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<GetProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GetProductWithDetailsResponseDTO> getProductDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductWithDetailsById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam("categoryName") String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllUniqueCategories() {
        return ResponseEntity.ok(productService.getAllUniqueCategories());
    }
}
