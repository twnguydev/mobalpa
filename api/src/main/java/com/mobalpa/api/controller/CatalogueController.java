package com.mobalpa.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.service.CatalogueService;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;


  @GetMapping("/categories")
  public ResponseEntity<?> getAllCategories() {
    try {
      List<CategoryDTO> categories = catalogueService.getAllCategories();
      return ResponseEntity.ok(categories);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/best-sellers")
  public ResponseEntity<?> getBestSellers() {
    try {
      List<ProductDTO> bestSellers = catalogueService.getBestSellers();
      return ResponseEntity.ok(bestSellers);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/products")
  public ResponseEntity<?> getAllProducts() {
    try {
      List<ProductDTO> products = catalogueService.getAllProducts();
      return ResponseEntity.ok(products);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/products/{productId}")
  public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
    try {
      ProductDTO product = catalogueService.getProductById(productId);
      return ResponseEntity.ok(product);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
    try {
      CategoryDTO category = catalogueService.getCategoryById(categoryId);
      return ResponseEntity.ok(category);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/subcategories/{subcategoryId}")
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID subcategoryId) {
    try {
      SubcategoryDTO subcategory = catalogueService.getSubcategoryById(subcategoryId);
      return ResponseEntity.ok(subcategory);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
