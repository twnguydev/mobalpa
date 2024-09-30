package com.mobalpa.api.dto.forecast;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SummaryDTO {

  @JsonProperty("Période")
  private String period;

  @JsonProperty("Date de début")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonProperty("Date de fin")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @JsonProperty("Nombre total de ventes")
  private Double totalSales;

  @JsonProperty("Quantité totale vendue")
  private Double totalQuantitySold;

  @JsonProperty("Total des frais de livraison")
  private Double totalDeliveryFees;

  @JsonProperty("Total des réductions")
  private Double totalDiscounts;

  @JsonProperty("Total de TVA")
  private Double totalVat;

  @JsonProperty("Total des ventes HT")
  private Double totalSalesExcludingTax;

  @JsonProperty("Total des ventes TTC")
  private Double totalSalesIncludingTax;
}