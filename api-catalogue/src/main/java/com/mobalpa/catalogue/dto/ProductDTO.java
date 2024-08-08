package com.mobalpa.catalogue.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDTO extends SimpleDTO {
  private String description;
  private Double price;
  private Integer stock;
  private LocalDateTime estimatedDelivery;
  private Double weight;
  private Double height;
  private Double width;
  private String uri;
  private LocalDateTime createdAt;
  private SimpleDTO category;
  private SimpleDTO subcategory;
  private SimpleDTO brand;
  private List<SimpleDTO> colors;
  private List<ImageDTO> images;
}