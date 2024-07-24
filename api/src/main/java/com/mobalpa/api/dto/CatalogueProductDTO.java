package com.mobalpa.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CatalogueProductDTO {

    private UUID uuid;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime createdAt;
    private CatalogueCategoryDTO category;
    private CatalogueBrandDTO brand;
    private double weight;
    private double height;
    private double width;
    private CatalogueColorDTO color;
}