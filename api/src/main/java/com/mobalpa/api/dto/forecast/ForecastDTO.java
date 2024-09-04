package com.mobalpa.api.dto.forecast;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@Data
public class ForecastDTO {

  @JsonProperty("Date de prédiction de vente")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate predictionDate;

  @JsonProperty("Fiabilité (%)")
  private Double reliability;

  @JsonProperty("Nom du produit")
  private String productName;

  @JsonProperty("Quantité de prédiction")
  private Integer predictedQuantity;

  @JsonProperty("Total TTC")
  private Double totalTTC;
}