package com.mobalpa.api.dto.delivery;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ParcelItemDTO {
    private UUID productUuid;
    private String description;
    private Integer quantity;
    private Double value;
    private Double weight;
    private Double width;
    private Double height;
    private Map<String, String> properties;
}