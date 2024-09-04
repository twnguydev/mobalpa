package com.mobalpa.api.controller;

import com.mobalpa.api.dto.SatisfactionRequestDTO;
import com.mobalpa.api.model.Satisfaction;
import com.mobalpa.api.service.SatisfactionService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/satisfaction")
@Tag(name = "Satisfaction", description = "APIs for managing satisfaction entries")
public class SatisfactionController {

    @Autowired
    private SatisfactionService satisfactionService;

    @PostMapping("/create")
    @Operation(summary = "Create satisfaction entry", description = "Creates a new satisfaction entry.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createSatisfaction(@RequestBody SatisfactionRequestDTO satisfactionRequest) {
        try {
            Satisfaction createdSatisfaction = satisfactionService.createSatisfaction(satisfactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSatisfaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all satisfaction entries", description = "Fetches all satisfaction entries.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllSatisfaction() {
        try {
            List<Satisfaction> satisfactions = satisfactionService.getAllSatisfaction();
            return ResponseEntity.ok(satisfactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the satisfaction entries.");
        }
    }

    @GetMapping("/home")
    @Operation(summary = "Get home satisfaction entries", description = "Fetches the first three satisfaction entries.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getHomeSatisfaction() {
        try {
            List<Satisfaction> satisfactions = satisfactionService.getFirstThreeSatisfactions();
            return ResponseEntity.ok(satisfactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the satisfaction entries.");
        }
    }

    @GetMapping("/product/{uuid}")
    @Operation(summary = "Get product satisfaction entries", description = "Fetches all satisfaction entries for a product.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getProductSatisfaction(@PathVariable("uuid") UUID productUuid) {
        try {
            List<Satisfaction> satisfactions = satisfactionService.getProductSatisfaction(productUuid);
            return ResponseEntity.ok(satisfactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the satisfaction entries.");
        }
    }
}
