package com.mobalpa.api.controller.admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.mobalpa.api.service.ForecastService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
public class ForecastController {
  
  @Autowired
  private ForecastService forecastService;

  @GetMapping("/forecast")
  public ResponseEntity<?> getAllForecasts(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.getForecast(reportType));
  }

  @GetMapping("/summary")
  public ResponseEntity<?> getAllSummaries(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.getSummary(reportType));
  }

  @GetMapping("/sales")
  public ResponseEntity<?> getAllSales(@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
    return ResponseEntity.ok(forecastService.getSales(startDate, endDate));
  }

  @GetMapping("/forecast/csv")
  public ResponseEntity<?> downloadForecastCsv(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.downloadCsv("forecast", reportType, ""));
  }

  @GetMapping("/summary/csv")
  public ResponseEntity<?> downloadSummaryCsv(@RequestParam(required = true) String reportType) {
    return ResponseEntity.ok(forecastService.downloadCsv("summary", reportType, ""));
  }

  @GetMapping("/sales/csv")
  public ResponseEntity<?> downloadSalesCsv(@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
    return ResponseEntity.ok(forecastService.downloadCsv("sales", startDate, endDate));
  }
}
