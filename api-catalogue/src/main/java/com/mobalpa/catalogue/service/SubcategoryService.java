package com.mobalpa.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobalpa.catalogue.repository.SubcategoryRepository;
import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubcategoryService {

  @Autowired
  private SubcategoryRepository subcategoryRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public Optional<List<Subcategory>> getAllSubcategories() {
    return Optional.of(subcategoryRepository.findAll());
  }

  public Optional<Subcategory> getSubcategoryById(UUID id) {
    return subcategoryRepository.findById(id);
  }

  public Subcategory createSubcategory(Subcategory subcategory) {
    if (subcategory.getName() == null)
      throw new IllegalArgumentException("Subcategory name is required");
    if (subcategory.getUri() == null)
      throw new IllegalArgumentException("Subcategory uri is required");
    if (subcategory.getDescription() == null)
      throw new IllegalArgumentException("Subcategory description is required");
    if (subcategory.getCategory() == null)
      throw new IllegalArgumentException("Subcategory category is required");

    Optional<Subcategory> existingSubcategory = subcategoryRepository.findByName(subcategory.getName());
    Optional<Subcategory> existingUri = subcategoryRepository.findByUri(subcategory.getUri());

    if (existingSubcategory.isPresent()) {
      throw new IllegalArgumentException("Subcategory with name " + subcategory.getName() + " already exists");
    } else if (existingUri.isPresent()) {
      throw new IllegalArgumentException("Subcategory with uri " + subcategory.getUri() + " already exists");
    }

    Category category = subcategory.getCategory();
    if (category != null) {
      Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
      if (existingCategory.isPresent()) {
        Category existingCategoryEntity = existingCategory.get();
        subcategory.setCategory(existingCategoryEntity);
        if (existingCategoryEntity.getSubcategories() == null) {
          existingCategoryEntity.setSubcategories(new ArrayList<>());
        }
        existingCategoryEntity.getSubcategories().add(subcategory);
        categoryRepository.save(existingCategoryEntity);
      } else {
        throw new IllegalArgumentException("Category with name " + category.getName() + " does not exist");
      }
    }

    String categoryUri = subcategory.getCategory().getUri();
    if (!categoryUri.endsWith("/"))
      categoryUri += "/";
    if (subcategory.getUri().startsWith("/"))
      subcategory.setUri(subcategory.getUri().substring(1));
    subcategory.setUri(categoryUri + subcategory.getUri());

    subcategory.setProducts(new ArrayList<>());
    return subcategoryRepository.save(subcategory);
  }

  public Subcategory updateSubcategory(UUID id, Subcategory subcategory) {
    return subcategoryRepository.findById(id).map(existingSubcategory -> {
      if (subcategory.getName() != null)
        existingSubcategory.setName(subcategory.getName());
      if (subcategory.getDescription() != null)
        existingSubcategory.setDescription(subcategory.getDescription());
      if (subcategory.getCategory() != null) {
        Category category = subcategory.getCategory();
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory.isPresent()) {
          Category existingCategoryEntity = existingCategory.get();
          existingSubcategory.setCategory(existingCategoryEntity);
          if (existingCategoryEntity.getSubcategories() == null) {
            existingCategoryEntity.setSubcategories(new ArrayList<>());
          }
          existingCategoryEntity.getSubcategories().add(existingSubcategory);
          categoryRepository.save(existingCategoryEntity);
        } else {
          throw new IllegalArgumentException("Category with name " + category.getName() + " does not exist");
        }
      }
      return subcategoryRepository.save(existingSubcategory);
    }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
  }

  public void deleteSubcategory(UUID id) {
    subcategoryRepository.deleteById(id);
  }
}
