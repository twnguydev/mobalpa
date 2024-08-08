package com.mobalpa.api.dto.delivery;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ParcelDTO {
    private UUID orderUuid;
    private List<ParcelItemDTO> parcelItems;
    private String shippingMethodCheckoutName;
    private String senderAddress;
    private String recipientAddress;
    private String recipientPhoneNumber;
    private String recipientEmail;
    private String recipientName;
    private ShipmentDTO shipment;
}