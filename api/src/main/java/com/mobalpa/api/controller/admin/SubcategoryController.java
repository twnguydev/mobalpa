package com.mobalpa.api.controller.admin;

import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/subcategories")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
public class SubcategoryController {

    @Autowired
    private CatalogueService catalogueService;

    @GetMapping
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories() {
        List<SubcategoryDTO> subcategories = catalogueService.getAllSubcategories();
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(@PathVariable UUID id) {
        SubcategoryDTO subcategory = catalogueService.getSubcategoryById(id);
        if (subcategory != null) {
            return new ResponseEntity<>(subcategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
