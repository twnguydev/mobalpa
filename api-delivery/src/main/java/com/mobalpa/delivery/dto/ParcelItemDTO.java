package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ParcelItemDTO {
    private String description;
    private UUID productUuid;
    private Map<String, String> properties;
    private Integer quantity;
    private Double value;
    private Double weight;
    private Double width;
    private Double height;
}