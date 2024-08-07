package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ParcelDTO {
    private UUID orderUuid;
    private List<ParcelItemDTO> parcelItems;
    private String shippingMethodCheckoutName;
    private String senderAddress;
}