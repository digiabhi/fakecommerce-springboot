package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateProductRequestDTO;
import com.example.fakecommerce.dtos.GetProductResponseDTO;
import com.example.fakecommerce.dtos.GetProductWithDetailsResponseDTO;
import com.example.fakecommerce.exceptions.ResourceDeletionException;
import com.example.fakecommerce.exceptions.InvalidRequestException;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.schema.Category;
import com.example.fakecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<GetProductResponseDTO> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();

        // Stream API
        return products.stream()
                .map(product -> GetProductResponseDTO.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .image(product.getImage())
                        .rating(product.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public GetProductResponseDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .map(product -> GetProductResponseDTO.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .image(product.getImage())
                        .rating(product.getRating())
                        .build())
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ResourceNotFoundException("Product with id " + id + " not found");
                });
    }

    public GetProductWithDetailsResponseDTO getProductWithDetailsById(Long id) {
        log.info("Fetching product with details for id: {}", id);
        Product product = productRepository.findProductsWithDetailsById(id).stream()
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Product with id {} not found for details view", id);
                    return new ResourceNotFoundException("Product with id " + id + " not found");
                });

        return GetProductWithDetailsResponseDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory().getName())
                .image(product.getImage())
                .rating(product.getRating())
                .build();
    }

    public Product createProduct(CreateProductRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.getCategoryId() == null) {
            log.error("Failed to create product: categoryId is required");
            throw new InvalidRequestException("Product categoryId is required");
        }
        log.info("Creating product with title: {} in categoryId: {}", requestDTO.getTitle(), requestDTO.getCategoryId());

        Category category = categoryService.getCategoryById(requestDTO.getCategoryId());

        Product newProduct = Product.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .image(requestDTO.getImage())
                .category(category)
                .price(requestDTO.getPrice())
                .rating(requestDTO.getRating())
                .build();

        Product savedProduct = productRepository.save(newProduct);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = getProductEntityById(id);
        try {
            productRepository.delete(product);
            log.info("Product with id {} deleted successfully", id);
        } catch (DataAccessException ex) {
            log.error("Error deleting product with id {}", id, ex);
            throw new ResourceDeletionException("Product with id " + id + " could not be deleted", ex);
        }
    }

    private Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ResourceNotFoundException("Product with id " + id + " not found");
                });
    }

    public List<Product> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findAllByCategory(category);
    }

    public List<String> getAllUniqueCategories() {
        log.info("Fetching all unique categories");
        return productRepository.findAllUniqueCategories();
    }
}
