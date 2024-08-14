package com.mobalpa.catalogue.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDTO extends SimpleDTO {
  private String description;
  private Double price;
  private Integer stock;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime estimatedDelivery;

  private Double weight;
  private Double height;
  private Double width;
  private String uri;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
  
  private SimpleDTO category;
  private SimpleDTO subcategory;
  private SimpleDTO brand;
  private List<SimpleDTO> colors;
  private List<ImageDTO> images;
}