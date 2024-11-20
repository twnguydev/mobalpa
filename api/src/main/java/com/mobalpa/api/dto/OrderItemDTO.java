package com.mobalpa.api.dto;

import lombok.Data;

import java.util.UUID;
import java.util.Map;

@Data
public class OrderItemDTO {
  private UUID uuid;
  private UUID productUuid;
  private Integer quantity;
  private Map<String, String> properties;
}