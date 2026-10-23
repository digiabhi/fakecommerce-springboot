package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateProductRequestDTO;
import com.example.fakecommerce.dtos.GetProductResponseDTO;
import com.example.fakecommerce.dtos.GetProductWithDetailsResponseDTO;
import com.example.fakecommerce.schema.Product;
import com.example.fakecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<GetProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public GetProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/details")
    public GetProductWithDetailsResponseDTO getProductDetailsById(@PathVariable Long id) {
        return productService.getProductWithDetailsById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequestDTO requestDTO) {
        return productService.createProduct(requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<Product> getProductsByCategory(@RequestParam("categoryName") String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/categories")
    public List<String> getAllUniqueCategories() {
        return productService.getAllUniqueCategories();
    }
}
