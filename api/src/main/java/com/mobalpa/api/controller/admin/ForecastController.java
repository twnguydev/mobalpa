package com.mobalpa.api.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.mobalpa.api.service.ForecastService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Business analysis management for admin users", description = "APIs for managing forecasts for admin users")
public class ForecastController {
  
  @Autowired
  private ForecastService forecastService;

  @GetMapping("/forecast")
  @Operation(summary = "Get all forecasts", description = "Fetches all forecasts.")
  public ResponseEntity<?> getAllForecasts(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.getForecast(reportType));
  }

  @GetMapping("/summary")
  @Operation(summary = "Get all summaries", description = "Fetches all summaries.")
  public ResponseEntity<?> getAllSummaries(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.getSummary(reportType));
  }

  @GetMapping("/sales")
  @Operation(summary = "Get all sales", description = "Fetches all sales.")
  public ResponseEntity<?> getAllSales(@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
    return ResponseEntity.ok(forecastService.getSales(startDate, endDate));
  }

  @GetMapping("/forecast/csv")
  @Operation(summary = "Download forecast CSV", description = "Downloads a CSV file of the forecast.")
  public ResponseEntity<?> downloadForecastCsv(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.downloadCsv("forecast", reportType, ""));
  }

  @GetMapping("/summary/csv")
  @Operation(summary = "Download summary CSV", description = "Downloads a CSV file of the summary.")
  public ResponseEntity<?> downloadSummaryCsv(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.downloadCsv("summary", reportType, ""));
  }

  @GetMapping("/sales/csv")
  @Operation(summary = "Download sales CSV", description = "Downloads a CSV file of the sales.")
  public ResponseEntity<?> downloadSalesCsv(@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
    return ResponseEntity.ok(forecastService.downloadCsv("sales", startDate, endDate));
  }
}
