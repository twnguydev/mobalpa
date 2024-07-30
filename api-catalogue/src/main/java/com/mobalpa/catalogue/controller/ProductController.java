package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogue/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<Product>> getProductsByStoreId(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductsByStoreId(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(id));
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<List<Product>> getProductsBySubcategoryId(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductsBySubcategoryId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return productService.getProductById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}