package com.mobalpa.api.dto.delivery;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeliveryDTO {

    private UUID uuid = UUID.randomUUID();
    private UUID orderUuid;
    private String address;
    private String deliveryMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}