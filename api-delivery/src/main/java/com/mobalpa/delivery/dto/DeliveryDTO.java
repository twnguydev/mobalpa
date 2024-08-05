package com.mobalpa.delivery.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeliveryDTO {
  private UUID deliveryId;
  private UUID orderId;
  private String address;
  private String deliveryMethod;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
