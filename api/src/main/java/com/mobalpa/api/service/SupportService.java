package com.mobalpa.api.service;


import com.mobalpa.api.dto.TicketRequestDTO;
import com.mobalpa.api.model.Ticket;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.TicketRepository;
import com.mobalpa.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupportService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket createTicket(TicketRequestDTO ticketRequestDTO) {
        if (ticketRequestDTO.getUserUuid() == null) {
            throw new IllegalArgumentException("User UUID must not be null");
        }

        Optional<User> user = userRepository.findById(ticketRequestDTO.getUserUuid());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user.get());
        ticket.setType(ticketRequestDTO.getType());
        ticket.setName(ticketRequestDTO.getName());
        ticket.setIssue(ticketRequestDTO.getIssue());
        ticket.setCreatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found");
        }
        return tickets;
    }

    public Ticket getTicketByUuid(UUID uuid) {
        return ticketRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getTicketsByUser(UUID userUuid) {
        Optional<User> user = userRepository.findById(userUuid);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        List<Ticket> tickets = ticketRepository.findByUser(user.get());
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found for user UUID: " + userUuid);
        }
        return tickets;
    }

    public Ticket resolveTicket(UUID ticketUuid, Map<String, String> resolutionData) {
        Ticket ticket = ticketRepository.findById(ticketUuid)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        String responderUuidStr = resolutionData.get("responderUuid");
        String resolution = resolutionData.get("resolution");

        if (responderUuidStr == null || resolution == null) {
            throw new IllegalArgumentException("Missing resolver UUID or resolution");
        }

        UUID responderUuid = UUID.fromString(responderUuidStr);

        Optional<User> responder = userRepository.findById(responderUuid);
        if (responder.isEmpty()) {
            throw new RuntimeException("Responder not found");
        }

        ticket.setResolution(resolution);
        ticket.setResponder(responder.get());
        ticket.setClosedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}