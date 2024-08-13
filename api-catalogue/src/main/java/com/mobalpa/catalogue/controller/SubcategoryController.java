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

@RestController
@RequestMapping("/api/catalogue/subcategories")
public class SubcategoryController {

  @Autowired
  private SubcategoryService subcategoryService;

  @GetMapping
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
