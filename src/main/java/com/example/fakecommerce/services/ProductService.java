package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateProductRequestDTO;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(CreateProductRequestDTO requestDTO) {
        Product newProduct = Product.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .image(requestDTO.getImage())
                .category(requestDTO.getCategory())
                .price(requestDTO.getPrice())
                .rating(requestDTO.getRating())
                .build();

        return productRepository.save(newProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }

    public List<String> getAllUniqueCategories() {
        return productRepository.findAllUniqueCategories();
    }
}
