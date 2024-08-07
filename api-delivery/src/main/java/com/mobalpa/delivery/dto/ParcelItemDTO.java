package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ParcelItemDTO {
    private String description;
    private String productId;
    private Map<String, String> properties;
    private Integer quantity;
    private Double value;
    private Double weight;
    private Double width;
    private Double height;
}