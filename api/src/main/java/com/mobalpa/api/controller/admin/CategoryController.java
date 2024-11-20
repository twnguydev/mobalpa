package com.mobalpa.api.controller.admin;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
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
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Category management for admin users", description = "APIs for managing categories for admin users")
public class CategoryController {

    @Autowired
    private CatalogueService catalogueService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Fetches all categories.")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = catalogueService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Fetches a category by its unique identifier.")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        CategoryDTO category = catalogueService.getCategoryById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
