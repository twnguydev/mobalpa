package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.service.CategoryService;
import com.mobalpa.catalogue.model.Subcategory;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<?> getAllCategories() {
    Optional<List<Category>> categories = categoryService.getAllCategories();
    if (categories.isPresent() && !categories.get().isEmpty()) {
      return ResponseEntity.ok(categories.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories found");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
    Optional<Category> category = categoryService.getCategoryById(id);
    if (category.isPresent() && category.get() != null) {
      return ResponseEntity.ok(category.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }
  }

  @GetMapping("/{id}/sub")
  public ResponseEntity<?> getSubcategoriesByCategoryId(@PathVariable UUID id) {
    Optional<List<Subcategory>> subcategories = Optional.of(categoryService.getSubcategoriesByCategoryId(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      return ResponseEntity.ok(subcategories.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found for this category");
    }
  }

  @PostMapping
  public ResponseEntity<?> createCategory(@RequestBody Category category) {
    try {
      Category createdCategory = categoryService.createCategory(category);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
    return ResponseEntity.ok(categoryService.updateCategory(id, category));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}