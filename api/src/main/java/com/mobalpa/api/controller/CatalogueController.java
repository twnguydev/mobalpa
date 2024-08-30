package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.service.PromotionService;
import com.mobalpa.api.filter.ProductFilter;
import com.mobalpa.api.dto.ProductWithCampaignDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.CategoryWithCampaignDTO;
import com.mobalpa.api.dto.SubcategoryWithCampaignDTO;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/catalogue")
@Tag(name = "Catalogue", description = "APIs for managing the catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @Autowired
  private PromotionService promotionService;

  @GetMapping("/categories")
  @Operation(summary = "Get all categories", description = "Fetches all categories.")
  public ResponseEntity<?> getAllCategories() {
    return ResponseEntity.ok(catalogueService.getAllCategories());
  }

  @GetMapping("/best-sellers")
  @Operation(summary = "Get best sellers", description = "Fetches the best selling products.")
  public ResponseEntity<?> getBestSellers() {
    return ResponseEntity.ok(catalogueService.getBestSellers());
  }

  @GetMapping("/coupons")
  @Operation(summary = "Get all coupons", description = "Fetches all coupons.")
  public ResponseEntity<?> getAllCoupons() {
    return ResponseEntity.ok(promotionService.getAllCoupons());
  }
  
  @GetMapping("/campaigns")
  @Operation(summary = "Get all campaigns", description = "Fetches all campaigns.")
  public ResponseEntity<?> getAllCampaigns() {
    return ResponseEntity.ok(promotionService.getAllCampaigns());
  }

  @GetMapping("/products")
  @Operation(summary = "Get all products", description = "Fetches all products.")
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

  @GetMapping("/products/{productUuid}/campaigns")
  @Operation(summary = "Get product campaigns", description = "Fetches the campaigns for a product.")
  public ResponseEntity<?> getProductCampaigns(@PathVariable UUID productUuid) {
    return ResponseEntity.ok(promotionService.getProductCampaigns(productUuid));
  }

  @GetMapping("/products-with-campaigns")
  @Operation(summary = "Get all products with campaigns", description = "Fetches all products with campaigns.")
  public ResponseEntity<?> getAllProductsWithCampaigns() {
    try {
      ProductFilter productFilter = new ProductFilter();
      List<ProductWithCampaignDTO> productsWithCampaigns = catalogueService.getAllProductsWithCampaign(productFilter);
      return ResponseEntity.ok(productsWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/subcategories-with-campaigns")
  @Operation(summary = "Get all subcategories with campaigns", description = "Fetches all subcategories with campaigns.")
  public ResponseEntity<?> getAllSubcategoriesWithCampaigns() {
    try {
      List<SubcategoryWithCampaignDTO> subcategoriesWithCampaigns = catalogueService.getAllSubcategoriesWithCampaign();
      return ResponseEntity.ok(subcategoriesWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/categories-with-campaigns")
  @Operation(summary = "Get all categories with campaigns", description = "Fetches all categories with campaigns.")
  public ResponseEntity<?> getAllCategoriesWithCampaigns() {
    try {
      List<CategoryWithCampaignDTO> categoriesWithCampaigns = catalogueService.getAllCategoriesWithCampaign();
      return ResponseEntity.ok(categoriesWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/products/{productId}")
  @Operation(summary = "Get product by UUID", description = "Fetches a product by its unique identifier.")
  public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
    return ResponseEntity.ok(catalogueService.getProductById(productId));
  }

  @GetMapping("/categories/{categoryId}")
  @Operation(summary = "Get category by UUID", description = "Fetches a category by its unique identifier.")
  public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(catalogueService.getCategoryById(categoryId));
  }

  @GetMapping("/categories/{categoryId}/products")
  @Operation(summary = "Get products by category", description = "Fetches the products for a category.")
  public ResponseEntity<?> getProductsByCategory(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(catalogueService.getProductsByCategoryId(categoryId));
  }

  @GetMapping("/subcategories")
  @Operation(summary = "Get all subcategories", description = "Fetches all subcategories.")
  public ResponseEntity<?> getAllSubcategories() {
    return ResponseEntity.ok(catalogueService.getAllSubcategories());
  }

  @GetMapping("/subcategories/{subcategoryId}/products")
  @Operation(summary = "Get products by subcategory", description = "Fetches the products for a subcategory.")
  public ResponseEntity<?> getProductsBySubcategory(
      @PathVariable UUID subcategoryId,
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
      productFilter.setSubcategoryId(subcategoryId);

      List<ProductDTO> products = catalogueService.getAllProducts(productFilter);
      return ResponseEntity.ok(products);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/subcategories/{subcategoryId}")
  @Operation(summary = "Get subcategory by UUID", description = "Fetches a subcategory by its unique identifier.")
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID subcategoryId) {
    return ResponseEntity.ok(catalogueService.getSubcategoryById(subcategoryId));
  }
}