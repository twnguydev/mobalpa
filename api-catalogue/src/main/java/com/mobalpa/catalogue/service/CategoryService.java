package com.mobalpa.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobalpa.catalogue.repository.CategoryRepository;
import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.model.Subcategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Collections;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID id, Category category) {
        category.setUuid(id);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(UUID id) {
        return categoryRepository.findById(id).map(Category::getSubcategories).orElse(Collections.emptyList());
    }
}
