package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.service.ProductService;
import com.mobalpa.catalogue.model.Store;
import com.mobalpa.catalogue.service.CatalogueService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.catalogue.filter.ProductFilter;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalogue/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CatalogueService catalogueService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "color", required = false) String color) {

        ProductFilter productFilter = new ProductFilter();
        productFilter.setMaxPrice(maxPrice);
        productFilter.setMinPrice(minPrice);
        productFilter.setBrandName(brand);
        productFilter.setColorName(color);

        List<Product> products = productService.getAllProducts(productFilter);
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<?> getProductsByStoreId(@PathVariable UUID id) {
        Optional<Store> store = catalogueService.getStoreById(id);
        if (store.isPresent() && store.get() != null) {
            Optional<List<Product>> products = Optional.of(store.get().getProducts());
            if (products.isPresent() && !products.get().isEmpty()) {
                return ResponseEntity.ok(products.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this store");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found");
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable UUID id) {
        Optional<List<Product>> products = Optional.of(productService.getProductsByCategoryId(id));
        if (products.isPresent() && !products.get().isEmpty()) {
            return ResponseEntity.ok(products.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this category");
        }
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<?> getProductsBySubcategoryId(@PathVariable UUID id) {
        Optional<List<Product>> products = Optional.of(productService.getProductsBySubcategoryId(id));
        if (products.isPresent() && !products.get().isEmpty()) {
            return ResponseEntity.ok(products.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this subcategory");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent() && product.get() != null) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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