package com.mobalpa.delivery.model;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery")
@Data
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID deliveryId;

  @Column(nullable = false, updatable = false)
  private UUID orderId;

  @Column(nullable = false, updatable = false)
  private String address;

  @Column(nullable = false, updatable = false)
  private String deliveryMethod;

  @Column(nullable = false)
  private StatusType status;

  public enum StatusType {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    CANCELED
  }

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = true)
  private LocalDateTime updatedAt;
}