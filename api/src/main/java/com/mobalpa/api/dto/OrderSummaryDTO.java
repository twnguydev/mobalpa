package com.mobalpa.api.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderSummaryDTO {
    private UUID uuid;
    private UUID userUuid;
    private UUID paymentUuid;
    private String deliveryAddress;
    private Double totalTtc;
    private String status;
    private List<String> deliveryNumbers;
    private List<OrderItemDTO> items;
}
