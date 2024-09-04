package com.mobalpa.api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SatisfactionDTO {
    private UUID uuid;
    private UUID userUuid;
    private UUID targetUuid;
    private String targetType;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}