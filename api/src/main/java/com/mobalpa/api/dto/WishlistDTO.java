package com.mobalpa.api.dto;

import lombok.Data;

import java.util.UUID;
import java.util.List;

@Data
public class WishlistDTO {
    private Integer id;
    private UUID userUuid;
    private List<CatalogueProductDTO> items;
}