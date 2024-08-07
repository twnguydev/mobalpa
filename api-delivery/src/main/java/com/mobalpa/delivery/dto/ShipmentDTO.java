package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ShipmentDTO {
    private String deliveryNumber;
    private UUID orderUuid;
    private String name;
}