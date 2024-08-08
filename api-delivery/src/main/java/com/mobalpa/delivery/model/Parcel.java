package com.mobalpa.delivery.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parcels")
@Data
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String name = "John Doe";
    private String companyName = "Mobalpa";
    private String email = "john.doe@example.com";
    private String telephone = "+33612345678";
    private String address = "1, rue de la Paix";
    private String houseNumber = "1";
    private String address2 = "Bâtiment B";
    private String city = "Marseille";
    private String country = "FR";
    private String postalCode = "13001";
    private Double weight;
    private Double width;
    private Double height;
    private Double totalOrderValue;
    private String totalOrderValueCurrency = "EUR";
    private String shippingMethodCheckoutName;
    private String senderAddress;
    private String recipientAddress;
    private String recipientPhoneNumber;
    private String recipientEmail;
    private String recipientName;
    private Integer quantity;
    private Double totalInsuredValue;
    private Boolean isReturn;
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment_delivery_number", referencedColumnName = "deliveryNumber")
    private Shipment shipment;

    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ParcelItem> parcelItems = new ArrayList<>();
}