package com.mobalpa.api.dto.delivery;

import lombok.Data;

import java.util.UUID;
import java.util.List;

@Data
public class DeliveryRequestDTO {

    private UUID orderUuid;
    private List<ParcelItemDTO> parcelItems;
    private String shippingMethodCheckoutName;
    private String senderAddress;
    private String recipientAddress;
    private String recipientPhoneNumber;
    private String recipientEmail;
    private String recipientName;
}