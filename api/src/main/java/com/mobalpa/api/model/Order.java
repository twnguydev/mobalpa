package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobalpa.api.dto.delivery.DeliveryDTO;

import java.io.IOException;

@Entity
@Table(name = "`order`")
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @ManyToOne
  private User user;

  @Column(nullable = true)
  private Date warranty;

  @Column(nullable = false)
  private String deliveryAddress;

  @Column(nullable = false)
  private Double reduction;

  @Column(nullable = false)
  private Double deliveryFees;

  @Column(nullable = false)
  private String deliveryMethod;

  @Column(nullable = false)
  private Double vat;

  @Column(nullable = false)
  private Double totalHt;

  @Column(nullable = false)
  private Double totalTtc;

  @Column(nullable = false)
  private String status = "PENDING";

  @Transient
  private List<OrderItem> items;

  @Column(name = "items", columnDefinition = "json")
  private String itemsJson;

  @Transient
  private List<DeliveryDTO> deliveries;

  @Column(name = "deliveries", columnDefinition = "json")
  private String deliveriesJson;

  @PrePersist
  @PreUpdate
  public void serializeFields() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    this.itemsJson = objectMapper.writeValueAsString(this.items);
    this.deliveriesJson = objectMapper.writeValueAsString(this.deliveries);
  }

  @PostLoad
  @PostPersist
  @PostUpdate
  public void deserializeFields() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    this.items = objectMapper.readValue(this.itemsJson,
        objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
    this.deliveries = objectMapper.readValue(this.deliveriesJson,
        objectMapper.getTypeFactory().constructCollectionType(List.class, DeliveryDTO.class));
  }

  @ManyToOne(fetch = FetchType.LAZY)
  private Payment payment;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  public void setStatus(String status) {
    this.status = status;
    if ("COMPLETED".equals(status) && this.warranty == null) {
      this.warranty = Date.from(LocalDateTime.now().plusYears(7).atZone(ZoneId.systemDefault()).toInstant());
    }
  }
}