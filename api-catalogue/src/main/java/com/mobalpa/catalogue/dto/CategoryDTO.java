package com.mobalpa.catalogue.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends SimpleDTO {
  private String description;
  private String uri;
  private List<SimpleDTO> subcategories;
}