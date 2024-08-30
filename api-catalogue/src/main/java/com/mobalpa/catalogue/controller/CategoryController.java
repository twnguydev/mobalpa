package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.service.CategoryService;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.dto.CategoryDTO;
import com.mobalpa.catalogue.mapper.Mapper;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/catalogue/categories")
@Tag(name = "Category", description = "APIs for managing categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  @Operation(
      summary = "Get all categories",
      description = "Fetches all categories.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<?> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories()
        .orElseThrow(() -> new RuntimeException("No categories found"));
    if (categories != null && !categories.isEmpty()) {
      List<CategoryDTO> categoriesDTO = categories.stream().map(Mapper::toCategoryDTO).collect(Collectors.toList());
      return ResponseEntity.ok(categoriesDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories found");
    }
  }

  @GetMapping("/{uuid}")
  @Operation(
      summary = "Get category by UUID",
      description = "Fetches a category by its unique identifier.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
    Optional<Category> category = categoryService.getCategoryById(id);
    if (category.isPresent() && category.get() != null) {
      return ResponseEntity.ok(category.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }
  }

  @GetMapping("/{id}/sub")
  @Operation(
      summary = "Get subcategories by category ID",
      description = "Fetches all subcategories by category UUID.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<?> getSubcategoriesByCategoryId(@PathVariable UUID id) {
    Optional<List<Subcategory>> subcategories = Optional.of(categoryService.getSubcategoriesByCategoryId(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      return ResponseEntity.ok(subcategories.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found for this category");
    }
  }

  @GetMapping("/{id}/products")
  @Operation(
      summary = "Get products by category ID",
      description = "Fetches all products by category UUID.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<?> getProductsByCategoryId(@PathVariable UUID id) {
    Optional<List<Subcategory>> subcategories = Optional.of(categoryService.getSubcategoriesByCategoryId(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      List<UUID> subcategoryIds = subcategories.get().stream().map(Subcategory::getUuid).collect(Collectors.toList());
      return ResponseEntity.ok(categoryService.getProductsBySubcategoryIds(subcategoryIds));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this category");
    }
  }

  @PostMapping
  @Operation(
      summary = "Create category",
      description = "Creates a new category.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<?> createCategory(@RequestBody Category category) {
    try {
      Category createdCategory = categoryService.createCategory(category);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  @Operation(
      summary = "Update category",
      description = "Updates an existing category.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
    return ResponseEntity.ok(categoryService.updateCategory(id, category));
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete category",
      description = "Deletes a category.",
      security = @SecurityRequirement(name = "apiKey")
  )
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}