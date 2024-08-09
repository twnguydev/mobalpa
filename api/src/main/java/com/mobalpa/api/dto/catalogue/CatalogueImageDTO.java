package com.mobalpa.api.dto.catalogue;

import lombok.Data;

import java.util.UUID;

@Data
public class CatalogueImageDTO {

      private UUID uuid;
      private String uri;
      private ColorDTO color;
}