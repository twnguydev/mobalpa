package com.mobalpa.delivery.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parcels")
@Data
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
    private Integer toServicePoint;
    private Integer toPostNumber;
    private String customsInvoiceNr;
    private String customsShipmentType;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private Double totalOrderValue;
    private String totalOrderValueCurrency;
    private String shippingMethodCheckoutName;
    private Integer senderAddress;
    private Integer quantity;
    private Double totalInsuredValue;
    private Boolean isReturn;
    private Boolean requestLabel;
    private Boolean applyShippingRules;
    private Boolean requestLabelAsync;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    private List<ParcelItem> parcelItems;

    @Embedded
    private Shipment shipment;
}