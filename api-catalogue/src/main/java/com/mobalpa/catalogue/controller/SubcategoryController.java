package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.service.SubcategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/subcategories")
public class SubcategoryController {

  @Autowired
  private SubcategoryService subcategoryService;

  @GetMapping
  public ResponseEntity<List<Subcategory>> getAllSubcategories() {
    return ResponseEntity.ok(subcategoryService.getAllSubcategories());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Subcategory> getSubcategoryById(@PathVariable UUID id) {
    return subcategoryService.getSubcategoryById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> createSubcategory(@RequestBody Subcategory subcategory) {
    try {
      Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory);
      return new ResponseEntity<>(createdSubcategory, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Subcategory> updateSubcategory(@PathVariable UUID id, @RequestBody Subcategory subcategory) {
    return ResponseEntity.ok(subcategoryService.updateSubcategory(id, subcategory));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSubcategory(@PathVariable UUID id) {
    subcategoryService.deleteSubcategory(id);
    return ResponseEntity.noContent().build();
  }
}
