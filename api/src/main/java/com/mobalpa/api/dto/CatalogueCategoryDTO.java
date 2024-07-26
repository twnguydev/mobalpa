package com.mobalpa.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CatalogueCategoryDTO {

    private UUID uuid;
    private String name;
    private String description;
}