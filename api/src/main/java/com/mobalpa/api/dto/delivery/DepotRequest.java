package com.mobalpa.api.dto.delivery;

import lombok.Data;

@Data
public class DepotRequest {
  private String address;
  private Double price;
}
