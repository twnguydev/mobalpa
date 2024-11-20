package com.mobalpa.api.dto.catalogue;

import lombok.Data;

import java.util.UUID;

@Data
public class BrandDTO {

    private UUID uuid;
    private String name;
}