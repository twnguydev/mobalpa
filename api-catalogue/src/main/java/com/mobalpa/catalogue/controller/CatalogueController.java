package com.mobalpa.catalogue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.service.CatalogueService;
import com.mobalpa.catalogue.model.Store;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/catalogue")
@Tag(name = "Catalogue", description = "APIs for managing the catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/store/{storeId}")
  @Operation(summary = "Get products by store ID", description = "Fetches all products by store ID.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getProductsByStoreId(@PathVariable UUID storeId) {
    Optional<Store> store = catalogueService.getStoreById(storeId);
    if (store.isPresent() && store.get() != null) {
      return ResponseEntity.ok(store.get().getProducts());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found");
    }
  }

  @PostMapping("/store")
  @Operation(summary = "Create store", description = "Creates a new store.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> createStore(@RequestBody Store store) {
    try {
      Store createdStore = catalogueService.createStore(store);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/best-sellers")
  @Operation(summary = "Get best sellers", description = "Fetches the best selling products.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getBestSellers() {
    try {
      List<Product> bestSellers = catalogueService.getBestSellers();
      if (bestSellers.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No best sellers found");
      } else {
        return ResponseEntity.ok(bestSellers);
      }
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}