package com.mobalpa.api.service;

import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.dto.catalogue.ImageDTO;
import com.mobalpa.api.dto.catalogue.ColorDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class DeliveryService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${delivery.base-url}")
  private String baseUrl;

  @Value("${delivery.api-key}")
  private String apiKey;

  @Autowired
  private CatalogueService catalogueService;

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

  public DeliveryDTO updateDeliveryStatus(String id, String status) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", this.apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(status, headers);

    ResponseEntity<DeliveryDTO> response = restTemplate.exchange(
        this.baseUrl + id, HttpMethod.PUT, request, DeliveryDTO.class);
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

  public ParcelDTO processDelivery(OrderRequestDTO orderRequestDTO, Order order, DepotDTO depot, User user) {
    DeliveryRequestDTO deliveryRequest = new DeliveryRequestDTO();
    deliveryRequest.setOrderUuid(order.getUuid());
    deliveryRequest.setParcelItems(orderRequestDTO.getItems().stream().map(itemDTO -> {
      ParcelItemDTO parcelItemDTO = new ParcelItemDTO();
      ProductDTO product = catalogueService.getProductById(itemDTO.getProductUuid());
      parcelItemDTO.setDescription(product.getDescription());
      parcelItemDTO.setProductUuid(product.getUuid());
      Map<String, String> properties = new HashMap<>();
      properties.put("brand", product.getBrand().getName());
      properties.put("colors", product.getColors().stream().map(ColorDTO::getName).collect(Collectors.joining(", ")));
      properties.put("images",
          product.getImages().stream().map(ImageDTO::getUri).collect(Collectors.joining(", ")));
      parcelItemDTO.setProperties(properties);
      parcelItemDTO.setQuantity(itemDTO.getQuantity());
      parcelItemDTO.setValue(product.getPrice());
      parcelItemDTO.setWeight(product.getWeight());
      parcelItemDTO.setWidth(product.getWidth());
      parcelItemDTO.setHeight(product.getHeight());
      return parcelItemDTO;
    }).collect(Collectors.toList()));
    deliveryRequest.setShippingMethodCheckoutName(orderRequestDTO.getDeliveryMethod());
    deliveryRequest.setSenderAddress(depot.getAddress());
    deliveryRequest.setRecipientAddress(orderRequestDTO.getDeliveryAddress());
    deliveryRequest.setRecipientPhoneNumber(user.getPhoneNumber());
    deliveryRequest.setRecipientEmail(user.getEmail());
    deliveryRequest.setRecipientName(user.getFirstname() + " " + user.getLastname());

    ParcelDTO delivery = createDelivery(deliveryRequest);
    if (delivery == null) {
      throw new RuntimeException("Delivery creation failed");
    }

    return delivery;
  }
}