package com.mobalpa.delivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParcelDTO {
    private String name;
    private String companyName;
    private String email;
    private String telephone;
    private String address;
    private String houseNumber;
    private String address2;
    private String city;
    private String country;
    private String postalCode;
    private String countryState;
    private String toServicePoint;
    private String toPostNumber;
    private String customsInvoiceNr;
    private String customsShipmentType;
    private List<ParcelItemDTO> parcelItems;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private Double totalOrderValue;
    private String totalOrderValueCurrency;
    private ShipmentDTO shipment;
    private String shippingMethodCheckoutName;
    private String senderAddress;
    private Integer quantity;
    private Double totalInsuredValue;
    private Boolean isReturn;
    private Boolean requestLabel;
    private Boolean applyShippingRules;
    private Boolean requestLabelAsync;
}