package com.mobalpa.api.controller;

import com.mobalpa.api.dto.TicketRequestDTO;
import com.mobalpa.api.model.Ticket;
import com.mobalpa.api.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/ticket")
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
    public ResponseEntity<?> getAllTickets() {
        try {
            List<Ticket> tickets = supportService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ticket/{uuid}")
    public ResponseEntity<?> getTicket(@PathVariable UUID uuid) {
        try {
            Ticket ticket = supportService.getTicketByUuid(uuid);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ticket/user/{userUuid}")
    public ResponseEntity<?> getTicketsByUser(@PathVariable UUID userUuid) {
        try {
            List<Ticket> tickets = supportService.getTicketsByUser(userUuid);
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/ticket/{ticketUuid}/resolve")
    public ResponseEntity<?> resolveTicket(@PathVariable UUID ticketUuid, @RequestBody Map<String, String> resolutionData) {
        try {
            Ticket resolvedTicket = supportService.resolveTicket(ticketUuid, resolutionData);
            return ResponseEntity.ok(resolvedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
