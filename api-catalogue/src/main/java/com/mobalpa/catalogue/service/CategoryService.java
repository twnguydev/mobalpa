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

    public Optional<List<Category>> getAllCategories() {
        return Optional.of(categoryRepository.findAll());
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        Optional<Category> existingUri = categoryRepository.findByUri(category.getUri());
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        } else if (existingUri.isPresent()) {
            throw new IllegalArgumentException("Category with uri " + category.getUri() + " already exists");
        }

        if (category.getName() == null) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (category.getUri() == null) {
            throw new IllegalArgumentException("Category uri is required");
        }
        if (category.getDescription() == null) {
            throw new IllegalArgumentException("Category description is required");
        }
        
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID id, Category category) {
        return categoryRepository.findById(id).map(existingCategory -> {
            if (category.getName() != null) {
                existingCategory.setName(category.getName());
            }
            if (category.getDescription() != null) {
                existingCategory.setDescription(category.getDescription());
            }
            if (category.getSubcategories() != null) {
                existingCategory.setSubcategories(category.getSubcategories());
            }
            return categoryRepository.save(existingCategory);
        }).orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(UUID id) {
        return categoryRepository.findById(id).map(Category::getSubcategories).orElse(Collections.emptyList());
    }
}
