package com.mobalpa.api.catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.catalogue.model.Product;
import com.mobalpa.api.catalogue.service.CatalogueService;
import com.mobalpa.api.catalogue.model.Store;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/store/{storeId}")
  public ResponseEntity<Optional<Store>> getProductsByStoreId(@PathVariable UUID storeId) {
    return ResponseEntity.ok(catalogueService.getStoreById(storeId));
  }

  @GetMapping("/best-sellers")
  public ResponseEntity<List<Product>> getBestSellers() {
    return ResponseEntity.ok(catalogueService.getBestSellers());
  }
}