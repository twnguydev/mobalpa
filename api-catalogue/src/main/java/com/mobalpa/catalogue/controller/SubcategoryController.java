package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.service.SubcategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalogue/subcategories")
public class SubcategoryController {

  @Autowired
  private SubcategoryService subcategoryService;

  @GetMapping
  public ResponseEntity<?> getAllSubcategories() {
    Optional<List<Subcategory>> subcategories = subcategoryService.getAllSubcategories();
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      return ResponseEntity.ok(subcategories.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID id) {
    Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(id);
    if (subcategory.isPresent() && subcategory.get() != null) {
      return ResponseEntity.ok(subcategory.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subcategory not found");
    }
  }

  @PostMapping
  public ResponseEntity<?> createSubcategory(@RequestBody Subcategory subcategory) {
    try {
      Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateSubcategory(@PathVariable UUID id, @RequestBody Subcategory subcategory) {
    try {
      Subcategory updatedSubcategory = subcategoryService.updateSubcategory(id, subcategory);
      return ResponseEntity.ok(updatedSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSubcategory(@PathVariable UUID id) {
    subcategoryService.deleteSubcategory(id);
    return ResponseEntity.noContent().build();
  }
}
