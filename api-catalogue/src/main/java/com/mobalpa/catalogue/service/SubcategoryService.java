package com.mobalpa.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobalpa.catalogue.repository.SubcategoryRepository;
import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubcategoryService {
  
  @Autowired
  private SubcategoryRepository subcategoryRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Subcategory> getAllSubcategories() {
    return subcategoryRepository.findAll();
  }

  public Optional<Subcategory> getSubcategoryById(UUID id) {
    return subcategoryRepository.findById(id);
  }

  public Subcategory createSubcategory(Subcategory subcategory) {
    Optional<Subcategory> existingSubcategory = subcategoryRepository.findByName(subcategory.getName());
    if (existingSubcategory.isPresent()) {
      throw new IllegalArgumentException("Subcategory with name " + subcategory.getName() + " already exists");
    }

    Category category = subcategory.getCategory();
    if (category != null) {
      Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
      if (existingCategory.isPresent()) {
        subcategory.setCategory(existingCategory.get());
        category.getSubcategories().add(subcategory);
      } else {
        throw new IllegalArgumentException("Category with name " + category.getName() + " does not exist");
      }
    }
    return subcategoryRepository.save(subcategory);
  }

  public Subcategory updateSubcategory(UUID id, Subcategory subcategory) {
    subcategory.setUuid(id);
    return subcategoryRepository.save(subcategory);
  }

  public void deleteSubcategory(UUID id) {
    subcategoryRepository.deleteById(id);
  }
}
