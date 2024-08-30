package com.mobalpa.api.controller;

import com.mobalpa.api.dto.TicketRequestDTO;
import com.mobalpa.api.model.Ticket;
import com.mobalpa.api.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@Tag(name = "Support", description = "APIs for managing support tickets")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/ticket")
    @Operation(summary = "Create ticket", description = "Creates a new support ticket.")
    public ResponseEntity<?> createTicket(@RequestBody TicketRequestDTO ticketRequest) {
        try {
            Ticket createdTicket = supportService.createTicket(ticketRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ticket")
    @Operation(summary = "Get all tickets", description = "Fetches all support tickets.")
    public ResponseEntity<?> getAllTickets() {
        try {
            List<Ticket> tickets = supportService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ticket/{uuid}")
    @Operation(summary = "Get ticket by UUID", description = "Fetches a support ticket by its unique identifier.")
    public ResponseEntity<?> getTicket(@PathVariable UUID uuid) {
        try {
            Ticket ticket = supportService.getTicketByUuid(uuid);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ticket/user/{userUuid}")
    @Operation(summary = "Get tickets by user", description = "Fetches all support tickets for a user.")
    public ResponseEntity<?> getTicketsByUser(@PathVariable UUID userUuid) {
        try {
            List<Ticket> tickets = supportService.getTicketsByUser(userUuid);
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/ticket/{ticketUuid}/resolve")
    @Operation(summary = "Resolve ticket", description = "Resolves a support ticket.")
    public ResponseEntity<?> resolveTicket(@PathVariable UUID ticketUuid, @RequestBody Map<String, String> resolutionData) {
        try {
            Ticket resolvedTicket = supportService.resolveTicket(ticketUuid, resolutionData);
            return ResponseEntity.ok(resolvedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
