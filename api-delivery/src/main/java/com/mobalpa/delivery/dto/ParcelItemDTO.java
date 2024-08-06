package com.mobalpa.delivery.dto;

import lombok.Data;

@Data
public class ParcelItemDTO {
    private String description;
    private String hsCode;
    private String originCountry;
    private String productId;
    private String properties;
    private Integer quantity;
    private String sku;
    private Double value;
    private Double weight;
}