package com.mobalpa.delivery.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parcel_items")
@Data
public class ParcelItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;
    private String hsCode;
    private String originCountry;
    private String productId;
    private String properties;
    private Integer quantity;
    private String sku;
    private Double value;
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;
}