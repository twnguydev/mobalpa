package com.mobalpa.delivery.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "parcel_items")
@Data
public class ParcelItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String description;
    private String productId;
    private Integer quantity;
    private Double value;
    private Double weight;
    private Double width;
    private Double height;

    @ElementCollection
    @CollectionTable(name = "parcel_item_properties", joinColumns = @JoinColumn(name = "parcel_item_uuid"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    private Map<String, String> properties;

    @ManyToOne
    @JoinColumn(name = "parcel_uuid")
    private Parcel parcel;
}