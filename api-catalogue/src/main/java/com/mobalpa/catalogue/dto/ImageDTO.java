package com.mobalpa.catalogue.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ImageDTO {
    private UUID uuid;
    private String uri;
    private SimpleDTO color;
}