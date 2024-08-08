package com.mobalpa.api.dto.catalogue;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProductDTO {

    private UUID uuid;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime createdAt;
    private CategoryDTO category;
    private SubcategoryDTO subcategory;
    private BrandDTO brand;
    private double weight;
    private double height;
    private double width;
    private List<ColorDTO> colors;
    private List<CatalogueImageDTO> images;
}