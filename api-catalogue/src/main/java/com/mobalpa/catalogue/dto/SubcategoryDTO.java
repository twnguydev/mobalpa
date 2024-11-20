package com.mobalpa.catalogue.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubcategoryDTO extends SimpleDTO {
  private String description;
  private String uri;

  @JsonIgnore
  private SimpleDTO category;
  
  private List<SimpleDTO> products;
}