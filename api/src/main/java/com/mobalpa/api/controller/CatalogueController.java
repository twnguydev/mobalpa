package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.filter.ProductFilter;
import com.mobalpa.api.dto.catalogue.ProductDTO;

import java.util.List;
import java.util.UUID;

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
  public ResponseEntity<?> getAllProducts(
      @RequestParam(required = false) String color,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false) String brand) {
    try {
      ProductFilter productFilter = new ProductFilter();
      productFilter.setColorName(color);
      productFilter.setMinPrice(minPrice);
      productFilter.setMaxPrice(maxPrice);
      productFilter.setBrandName(brand);

      List<ProductDTO> products = catalogueService.getAllProducts(productFilter);
      return ResponseEntity.ok(products);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
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