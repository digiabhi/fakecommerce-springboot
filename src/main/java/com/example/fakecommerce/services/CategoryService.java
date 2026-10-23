package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateCategoryRequestDTO;
import com.example.fakecommerce.repositories.CategoryRepository;
import com.example.fakecommerce.schema.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(CreateCategoryRequestDTO requestDTO) {
        Category newCategory = Category.builder().name(requestDTO.getName()).build();
        return categoryRepository.save(newCategory);
    }

    public Category deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        categoryRepository.delete(category);
        return category;
    }
}
