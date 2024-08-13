package com.mobalpa.api.dto.catalogue;

import lombok.Data;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID uuid;
    private String name;
    private String description;
    private double price;
    private int stock;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime estimatedDelivery;

    private double weight;
    private double height;
    private double width;
    private String uri;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    private CategoryDTO category;
    private SubcategoryDTO subcategory;
    private BrandDTO brand;
    private List<ColorDTO> colors;
    private List<ImageDTO> images;
}