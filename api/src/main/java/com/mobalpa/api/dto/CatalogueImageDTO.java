package com.mobalpa.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CatalogueImageDTO {
  
      private UUID uuid;
      private UUID productUuid;
      private UUID colorUuid;
      private String url;
}
