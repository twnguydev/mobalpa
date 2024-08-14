package com.mobalpa.api.dto;

import com.mobalpa.api.model.OrderItem;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {
    private UUID userUuid;
    private UUID paymentUuid;
    private List<OrderItem> items;
    private String deliveryMethod;
    private String deliveryAddress;
    private Double reduction;
    private Double totalHt;
}
