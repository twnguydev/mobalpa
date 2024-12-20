package com.mobalpa.api.dto.catalogue;

import lombok.Data;

import java.util.UUID;

@Data
public class SubcategoryDTO {
    private UUID uuid;
    private String name;
    private String description;
    private String uri;
    // private List<ProductDTO> products;
    // private CategoryDTO category;
}