package com.mobalpa.api.dto.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SalesDTO {

  @JsonProperty("quantity")
  private Double totalQuantitySold;

  @JsonProperty("delivery_fees")
  private Double totalDeliveryFees;

  @JsonProperty("reduction")
  private Double totalDiscounts;

  @JsonProperty("vat")
  private Double totalVat;

  @JsonProperty("total_ht")
  private Double totalSalesExcludingTax;

  @JsonProperty("total_ttc")
  private Double totalSalesIncludingTax;
}