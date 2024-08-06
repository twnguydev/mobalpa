package com.mobalpa.catalogue.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class SimpleDTO {
  private UUID uuid;
  private String name;
}
