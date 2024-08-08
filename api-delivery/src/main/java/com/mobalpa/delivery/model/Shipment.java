package com.mobalpa.delivery.model;

import com.mobalpa.delivery.dto.ParcelDTO;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

@Entity
@Table(name = "shipment")
@Data
public class Shipment {

    @Id
    private String deliveryNumber;

    private UUID orderUuid;

    private String name;

    public Shipment() {}

    public Shipment(ParcelDTO parcel) {
        this.deliveryNumber = randomDeliveryNumber(parcel.getShippingMethodCheckoutName());
    }

    private String randomDeliveryNumber(String name) {
        if ("Mobalpa Centrale".equals(name)) {
            return "MOB_" + RandomStringUtils.randomAlphabetic(12);
        } else if (name != null && name.length() >= 3) {
            return name.substring(0, 3).toUpperCase() + "_" + RandomStringUtils.randomAlphanumeric(12);
        } else {
            return "UNK_" + RandomStringUtils.randomAlphanumeric(12);
        }
    }
}