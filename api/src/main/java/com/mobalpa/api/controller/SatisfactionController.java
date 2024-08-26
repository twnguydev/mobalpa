package com.mobalpa.api.controller;

import com.mobalpa.api.dto.SatisfactionRequestDTO;
import com.mobalpa.api.model.Satisfaction;
import com.mobalpa.api.service.SatisfactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/satisfaction")
public class SatisfactionController {

    @Autowired
    private SatisfactionService satisfactionService;

    @PostMapping("/create")
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
}
