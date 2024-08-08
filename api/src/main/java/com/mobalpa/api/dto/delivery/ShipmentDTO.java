package com.mobalpa.api.dto.delivery;

import lombok.Data;

import java.util.UUID;

@Data
public class ShipmentDTO {
    private String deliveryNumber;
    private UUID orderUuid;
    private String name;
    private String address;
}