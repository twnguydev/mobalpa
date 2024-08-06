package com.mobalpa.delivery.model;

import lombok.Data;

import jakarta.persistence.Embeddable;

@Embeddable
@Data
public class Shipment {
    private Integer id;
    private String name;
}
