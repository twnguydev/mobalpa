package com.mobalpa.api.controller;

import com.mobalpa.api.dto.SatisfactionRequestDTO;
import com.mobalpa.api.model.Satisfaction;
import com.mobalpa.api.service.SatisfactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SatisfactionControllerTest {

    @Mock
    private SatisfactionService satisfactionService;

    @InjectMocks
    private SatisfactionController satisfactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSatisfactionSuccess() {
        SatisfactionRequestDTO satisfactionRequest = new SatisfactionRequestDTO(); // Configurez avec les attributs nécessaires
        Satisfaction satisfaction = new Satisfaction(); // Configurez avec les attributs nécessaires

        when(satisfactionService.createSatisfaction(any(SatisfactionRequestDTO.class))).thenReturn(satisfaction);

        ResponseEntity<?> response = satisfactionController.createSatisfaction(satisfactionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(satisfaction, response.getBody());
    }

    @Test
    void testCreateSatisfactionBadRequest() {
        SatisfactionRequestDTO satisfactionRequest = new SatisfactionRequestDTO(); // Configurez avec les attributs nécessaires

        when(satisfactionService.createSatisfaction(any(SatisfactionRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = satisfactionController.createSatisfaction(satisfactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void testCreateSatisfactionNotFound() {
        SatisfactionRequestDTO satisfactionRequest = new SatisfactionRequestDTO(); // Configurez avec les attributs nécessaires

        when(satisfactionService.createSatisfaction(any(SatisfactionRequestDTO.class)))
            .thenThrow(new RuntimeException("Service not found"));

        ResponseEntity<?> response = satisfactionController.createSatisfaction(satisfactionRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Service not found", response.getBody());
    }

    @Test
    void testGetAllSatisfactionSuccess() {
        List<Satisfaction> satisfactions = List.of(new Satisfaction()); // Assurez-vous que la liste contient des objets valides

        when(satisfactionService.getAllSatisfaction()).thenReturn(satisfactions);

        ResponseEntity<?> response = satisfactionController.getAllSatisfaction();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(satisfactions, response.getBody());
    }

    @Test
    void testGetAllSatisfactionFailure() {
        when(satisfactionService.getAllSatisfaction())
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = satisfactionController.getAllSatisfaction();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while fetching the satisfaction entries.", response.getBody());
    }

    @Test
    void testGetHomeSatisfactionSuccess() {
        List<Satisfaction> satisfactions = List.of(new Satisfaction()); // Assurez-vous que la liste contient des objets valides

        when(satisfactionService.getFirstThreeSatisfactions()).thenReturn(satisfactions);

        ResponseEntity<?> response = satisfactionController.getHomeSatisfaction();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(satisfactions, response.getBody());
    }

    @Test
    void testGetHomeSatisfactionFailure() {
        when(satisfactionService.getFirstThreeSatisfactions())
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = satisfactionController.getHomeSatisfaction();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while fetching the satisfaction entries.", response.getBody());
    }

    @Test
    void testGetProductSatisfactionSuccess() {
        UUID productUuid = UUID.randomUUID();
        List<Satisfaction> satisfactions = List.of(new Satisfaction()); // Assurez-vous que la liste contient des objets valides

        when(satisfactionService.getProductSatisfaction(any(UUID.class))).thenReturn(satisfactions);

        ResponseEntity<?> response = satisfactionController.getProductSatisfaction(productUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(satisfactions, response.getBody());
    }

    @Test
    void testGetProductSatisfactionFailure() {
        UUID productUuid = UUID.randomUUID();

        when(satisfactionService.getProductSatisfaction(any(UUID.class)))
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = satisfactionController.getProductSatisfaction(productUuid);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while fetching the satisfaction entries.", response.getBody());
    }
}