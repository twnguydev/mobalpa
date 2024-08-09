package com.mobalpa.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mobalpa.api.service.CatalogueService;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;


  @GetMapping("/categories")
  public ResponseEntity<?> getAllCategories() {
    return ResponseEntity.ok(catalogueService.getAllCategories());
  }

  @GetMapping("/best-sellers")
  public ResponseEntity<?> getBestSellers() {
    return ResponseEntity.ok(catalogueService.getBestSellers());
  }

  @GetMapping("/products")
  public ResponseEntity<?> getAllProducts() {
    return ResponseEntity.ok(catalogueService.getAllProducts());
  }

  @GetMapping("/products/{productId}")
  public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
    return ResponseEntity.ok(catalogueService.getProductById(productId));
  }

  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(catalogueService.getCategoryById(categoryId));
  }

  @GetMapping("/subcategories/{subcategoryId}")
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID subcategoryId) {
    return ResponseEntity.ok(catalogueService.getSubcategoryById(subcategoryId));
  }
}
