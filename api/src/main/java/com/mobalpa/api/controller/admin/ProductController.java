package com.mobalpa.api.controller.admin;

import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Product management for admin users", description = "APIs for managing products for admin users")
public class ProductController {

    @Autowired
    private CatalogueService catalogueService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Fetches all products.")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = catalogueService.getAllProducts(null);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Fetches a product by its unique identifier.")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        ProductDTO product = catalogueService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Creates a new product.")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO newProduct = catalogueService.createProduct(productDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates a product.")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDetails) {
        catalogueService.updateProduct(id, productDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product.")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        catalogueService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
