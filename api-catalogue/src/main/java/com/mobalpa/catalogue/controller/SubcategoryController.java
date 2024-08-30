package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.service.SubcategoryService;
import com.mobalpa.catalogue.dto.SubcategoryDTO;
import com.mobalpa.catalogue.mapper.Mapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/catalogue/subcategories")
@Tag(name = "Subcategory", description = "APIs for managing subcategories")
public class SubcategoryController {

  @Autowired
  private SubcategoryService subcategoryService;

  @GetMapping
  @Operation(summary = "Get all subcategories", description = "Fetches all subcategories.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getAllSubcategories() {
    List<Subcategory> subcategories = subcategoryService.getAllSubcategories().orElseThrow(() -> new RuntimeException("No subcategories found"));
    if (subcategories != null && !subcategories.isEmpty()) {
      List<SubcategoryDTO> subcategoriesDTO = subcategories.stream().map(Mapper::toSubcategoryDTO).collect(Collectors.toList());
      return ResponseEntity.ok(subcategoriesDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found");
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get subcategory by ID", description = "Fetches a subcategory by its unique identifier.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID id) {
    Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(id);
    if (subcategory.isPresent() && subcategory.get() != null) {
      return ResponseEntity.ok(subcategory.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subcategory not found");
    }
  }

  @PostMapping
  @Operation(summary = "Create subcategory", description = "Creates a new subcategory.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> createSubcategory(@RequestBody Subcategory subcategory) {
    try {
      Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update subcategory", description = "Updates a subcategory.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> updateSubcategory(@PathVariable UUID id, @RequestBody Subcategory subcategory) {
    try {
      Subcategory updatedSubcategory = subcategoryService.updateSubcategory(id, subcategory);
      return ResponseEntity.ok(updatedSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/{id}/products")
  @Operation(summary = "Get products by subcategory ID", description = "Fetches all products by subcategory UUID.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getProductsBySubcategoryId(@PathVariable UUID id) {
    Optional<Optional<Subcategory>> subcategories = Optional.of(subcategoryService.getSubcategoryById(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      return ResponseEntity.ok(subcategoryService.getProductsBySubcategoryId(id));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this subcategory");
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete subcategory", description = "Deletes a subcategory.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<Void> deleteSubcategory(@PathVariable UUID id) {
    subcategoryService.deleteSubcategory(id);
    return ResponseEntity.noContent().build();
  }
}
