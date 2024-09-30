package com.mobalpa.api.controller;

import com.mobalpa.api.dto.TicketRequestDTO;
import com.mobalpa.api.model.Ticket;
import com.mobalpa.api.service.SupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupportControllerTest {

    @Mock
    private SupportService supportService;

    @InjectMocks
    private SupportController supportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicketSuccess() {
        TicketRequestDTO ticketRequest = new TicketRequestDTO();
        Ticket ticket = new Ticket();

        when(supportService.createTicket(any(TicketRequestDTO.class))).thenReturn(ticket);

        ResponseEntity<?> response = supportController.createTicket(ticketRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testCreateTicketBadRequest() {
        TicketRequestDTO ticketRequest = new TicketRequestDTO();

        when(supportService.createTicket(any(TicketRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = supportController.createTicket(ticketRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void testCreateTicketNotFound() {
        TicketRequestDTO ticketRequest = new TicketRequestDTO();

        when(supportService.createTicket(any(TicketRequestDTO.class)))
            .thenThrow(new RuntimeException("Service not found"));

        ResponseEntity<?> response = supportController.createTicket(ticketRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Service not found", response.getBody());
    }

    @Test
    void testGetAllTicketsSuccess() {
        List<Ticket> tickets = List.of(new Ticket());

        when(supportService.getAllTickets()).thenReturn(tickets);

        ResponseEntity<?> response = supportController.getAllTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }

    @Test
    void testGetAllTicketsNotFound() {
        when(supportService.getAllTickets())
            .thenThrow(new RuntimeException("Service not found"));

        ResponseEntity<?> response = supportController.getAllTickets();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Service not found", response.getBody());
    }

    @Test
    void testGetTicketSuccess() {
        UUID ticketUuid = UUID.randomUUID();
        Ticket ticket = new Ticket();

        when(supportService.getTicketByUuid(any(UUID.class))).thenReturn(ticket);

        ResponseEntity<?> response = supportController.getTicket(ticketUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testGetTicketNotFound() {
        UUID ticketUuid = UUID.randomUUID();

        when(supportService.getTicketByUuid(any(UUID.class)))
            .thenThrow(new RuntimeException("Ticket not found"));

        ResponseEntity<?> response = supportController.getTicket(ticketUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found", response.getBody());
    }

    @Test
    void testGetTicketsByUserSuccess() {
        UUID userUuid = UUID.randomUUID();
        List<Ticket> tickets = List.of(new Ticket());

        when(supportService.getTicketsByUser(any(UUID.class))).thenReturn(tickets);

        ResponseEntity<?> response = supportController.getTicketsByUser(userUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }

    @Test
    void testGetTicketsByUserNotFound() {
        UUID userUuid = UUID.randomUUID();

        when(supportService.getTicketsByUser(any(UUID.class)))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = supportController.getTicketsByUser(userUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testResolveTicketSuccess() {
        UUID ticketUuid = UUID.randomUUID();
        Map<String, String> resolutionData = Map.of("key", "value");
        Ticket ticket = new Ticket();

        when(supportService.resolveTicket(any(UUID.class), any(Map.class))).thenReturn(ticket);

        ResponseEntity<?> response = supportController.resolveTicket(ticketUuid, resolutionData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testResolveTicketNotFound() {
        UUID ticketUuid = UUID.randomUUID();
        Map<String, String> resolutionData = Map.of("key", "value");

        when(supportService.resolveTicket(any(UUID.class), any(Map.class)))
            .thenThrow(new RuntimeException("Ticket not found"));

        ResponseEntity<?> response = supportController.resolveTicket(ticketUuid, resolutionData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found", response.getBody());
    }
}