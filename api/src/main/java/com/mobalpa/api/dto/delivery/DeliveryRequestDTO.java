package com.mobalpa.api.dto.delivery;

import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryRequestDTO {

    private UUID orderUuid;
    private String address;
    private String deliveryMethod;
}
