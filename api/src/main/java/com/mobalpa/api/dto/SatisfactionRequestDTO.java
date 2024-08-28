package com.mobalpa.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SatisfactionRequestDTO {
    private UUID userUuid;
    private UUID targetUuid;
    private String targetType;
    private int rating;
    private String comment;
}
