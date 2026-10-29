package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateCategoryRequestDTO;
import com.example.fakecommerce.exceptions.ResourceDeletionException;
import com.example.fakecommerce.exceptions.InvalidRequestException;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.CategoryRepository;
import com.example.fakecommerce.schema.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));
    }

    public Category createCategory(CreateCategoryRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.getName() == null || requestDTO.getName().isBlank()) {
            throw new InvalidRequestException("Category name is required");
        }
        Category newCategory = Category.builder().name(requestDTO.getName()).build();
        return categoryRepository.save(newCategory);
    }

    public Category deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        try {
            categoryRepository.delete(category);
            log.info("Category with id {} deleted successfully", categoryId);
        } catch (DataAccessException ex) {
            log.error("Error deleting category with id {}", categoryId, ex);
            throw new ResourceDeletionException("Category with id " + categoryId + " could not be deleted", ex);
        }
        return category;
    }
}
