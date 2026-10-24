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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

//import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public List<GetProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

//        List<GetProductResponseDTO> responseDTOS = new ArrayList<>();
//        for(Product product : products) {
//            GetProductResponseDTO responseDTO = GetProductResponseDTO.builder()
//                    .id(product.getId())
//                    .title(product.getTitle())
//                    .description(product.getDescription())
//                    .price(product.getPrice())
//                    .image(product.getImage())
//                    .rating(product.getRating())
//                    .build();
//            responseDTOS.add(responseDTO);
//        }
//        return responseDTOS;

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
        return productRepository.findById(id)
                .map(product -> GetProductResponseDTO.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .image(product.getImage())
                        .rating(product.getRating())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    public GetProductWithDetailsResponseDTO getProductWithDetailsById(Long id) {
        Product product = productRepository.findProductsWithDetailsById(id).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

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
            throw new InvalidRequestException("Product categoryId is required");
        }

        Category category = categoryService.getCategoryById(requestDTO.getCategoryId());

        Product newProduct = Product.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .image(requestDTO.getImage())
                .category(category)
                .price(requestDTO.getPrice())
                .rating(requestDTO.getRating())
                .build();

        return productRepository.save(newProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductEntityById(id);
        try {
            productRepository.delete(product);
        } catch (DataAccessException ex) {
            throw new ResourceDeletionException("Product with id " + id + " could not be deleted", ex);
        }
    }

    private Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }

    public List<String> getAllUniqueCategories() {
        return productRepository.findAllUniqueCategories();
    }
}
