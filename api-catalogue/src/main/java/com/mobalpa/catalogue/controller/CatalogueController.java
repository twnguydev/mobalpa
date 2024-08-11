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

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/store/{storeId}")
  public ResponseEntity<?> getProductsByStoreId(@PathVariable UUID storeId) {
    Optional<Store> store = catalogueService.getStoreById(storeId);
    if (store.isPresent() && store.get() != null) {
      return ResponseEntity.ok(store.get().getProducts());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found");
    }
  }

  @PostMapping("/store")
  public ResponseEntity<?> createStore(@RequestBody Store store) {
    try {
      Store createdStore = catalogueService.createStore(store);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/best-sellers")
  public ResponseEntity<List<Product>> getBestSellers() {
    return ResponseEntity.ok(catalogueService.getBestSellers());
  }
}