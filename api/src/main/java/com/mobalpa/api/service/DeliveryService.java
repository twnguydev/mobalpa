package com.mobalpa.api.service;

import com.mobalpa.api.dto.delivery.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class DeliveryService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${delivery.base-url}")
  private String baseUrl;

  @Value("${delivery.api-key}")
  private String apiKey;

  public ParcelDTO createDelivery(DeliveryRequestDTO deliveryRequest) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<DeliveryRequestDTO> request = new HttpEntity<>(deliveryRequest, headers);

    ResponseEntity<ParcelDTO> response = restTemplate.postForEntity(
        this.baseUrl.substring(0, this.baseUrl.length() - 1),
        request,
        ParcelDTO.class);
    return response.getBody();
  }

  public DeliveryDTO getDeliveryById(UUID id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<DeliveryDTO> response = restTemplate.exchange(
        this.baseUrl + id, HttpMethod.GET, request, DeliveryDTO.class);
    return response.getBody();
  }

  public DeliveryDTO updateDeliveryStatus(UUID id, String status) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>("{\"status\": \"" + status + "\"}", headers);

    ResponseEntity<DeliveryDTO> response = restTemplate.exchange(
        this.baseUrl + id, HttpMethod.PATCH, request, DeliveryDTO.class);
    return response.getBody();
  }

  public TrackingDTO trackDelivery(UUID id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<TrackingDTO> response = restTemplate.exchange(
        this.baseUrl + id + "/track", HttpMethod.GET, request, TrackingDTO.class);
    return response.getBody();
  }

  public List<DepotDTO> getDeliveryPrices() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<DepotDTO[]> response = restTemplate.exchange(
        this.baseUrl + "depot", HttpMethod.GET, request, DepotDTO[].class);
    return Arrays.asList(response.getBody());
  }

  public Optional<DepotDTO> getDeliveryPrice(String method) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<DepotDTO> response = restTemplate.exchange(
        this.baseUrl + "depot/" + method, HttpMethod.GET, request, DepotDTO.class);
    return Optional.ofNullable(response.getBody());
  }
}