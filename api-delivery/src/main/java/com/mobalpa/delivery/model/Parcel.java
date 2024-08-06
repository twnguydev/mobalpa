package com.mobalpa.delivery.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "parcels")
@Data
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private Double totalOrderValue;
    private String totalOrderValueCurrency;
    private String shippingMethodCheckoutName;
    private String senderAddress;
    private Integer quantity;
    private Double totalInsuredValue;
    private Boolean isReturn;
    private Boolean requestLabel;
    private Boolean applyShippingRules;
    private Boolean requestLabelAsync;
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment_id", referencedColumnName = "id")
    private Shipment shipment;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<ParcelItem> parcelItems;
}
