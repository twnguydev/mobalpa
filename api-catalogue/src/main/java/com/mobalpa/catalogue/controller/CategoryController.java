package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.service.CategoryService;
import com.mobalpa.catalogue.model.Subcategory;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
    return categoryService.getCategoryById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/sub")
  public ResponseEntity<List<Subcategory>> getSubcategoriesByCategoryId(@PathVariable UUID id) {
    return ResponseEntity.ok(categoryService.getSubcategoriesByCategoryId(id));
  }

  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    return ResponseEntity.ok(categoryService.createCategory(category));
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