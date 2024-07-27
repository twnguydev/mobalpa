package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.service.SubcategoryService;
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
  public ResponseEntity<Subcategory> createSubcategory(@RequestBody Subcategory subcategory) {
    return ResponseEntity.ok(subcategoryService.createSubcategory(subcategory));
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
