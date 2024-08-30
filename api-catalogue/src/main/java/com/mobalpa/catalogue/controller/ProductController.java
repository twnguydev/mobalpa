package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.service.ProductService;
import com.mobalpa.catalogue.service.CatalogueService;
import com.mobalpa.catalogue.mapper.Mapper;
import com.mobalpa.catalogue.dto.ProductDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.catalogue.filter.ProductFilter;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/catalogue/products")
@Tag(name = "Product", description = "APIs for managing products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Fetches all products.", security = @SecurityRequirement(name = "apiKey"))
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
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
        }
    }

    @GetMapping("/store/{id}")
    @Operation(summary = "Get products by store ID", description = "Fetches all products by store ID.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProductsByStoreId(@PathVariable UUID id) {
        List<Product> products = productService.getProductsByCategoryId(id).orElseThrow(() -> new RuntimeException("No products found for this subcategory"));
        if (products != null && !products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found");
        }
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get products by category ID", description = "Fetches all products by category UUID.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable UUID id) {
        List<Product> products = productService.getProductsByCategoryId(id).orElseThrow(() -> new RuntimeException("No products found for this subcategory"));
        if (products != null && !products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this category");
        }
    }

    @GetMapping("/sub/{id}")
    @Operation(summary = "Get products by subcategory ID", description = "Fetches all products by subcategory UUID.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProductsBySubcategoryId(@PathVariable UUID id) {
        List<Product> products = productService.getProductsBySubcategoryId(id).orElseThrow(() -> new RuntimeException("No products found for this subcategory"));
        if (products != null && !products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this subcategory");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by UUID", description = "Fetches a product by its unique identifier.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent() && product.get() != null) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Creates a new product.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product.", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}