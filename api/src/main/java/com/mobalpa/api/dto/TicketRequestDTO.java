package com.mobalpa.api.dto;

import com.mobalpa.api.model.TicketType;

import lombok.Data;

import java.util.UUID;

@Data
public class TicketRequestDTO {
    private UUID userUuid;
    private TicketType type;
    private String name;
    private String issue;
}