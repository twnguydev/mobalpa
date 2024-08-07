package com.mobalpa.delivery.controller;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.model.Parcel;
import com.mobalpa.delivery.service.ParcelService;
import com.mobalpa.delivery.service.DeliveryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private DeliveryPriceService deliveryPriceService;

    @PostMapping
    public ResponseEntity<String> createParcel(@RequestBody ParcelDTO parcelDTO) {
        try {
            Parcel createdParcel = parcelService.createParcel(parcelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParcel.getShipment().getDeliveryNumber());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{deliveryNumber}")
    public ResponseEntity<Parcel> getParcelByDeliveryNumber(@PathVariable String deliveryNumber) {
        Parcel parcel = parcelService.getParcelByDeliveryNumber(deliveryNumber);
        return ResponseEntity.ok(parcel);
    }

    @PatchMapping("/{deliveryNumber}")
    public ResponseEntity<Parcel> updateParcelStatus(@PathVariable String deliveryNumber, @RequestBody String status) {
        Parcel updatedParcel = parcelService.updateParcelStatus(deliveryNumber, status);
        return ResponseEntity.ok(updatedParcel);
    }

    @DeleteMapping("/{deliveryNumber}")
    public ResponseEntity<Void> deleteParcel(@PathVariable String deliveryNumber) {
        parcelService.deleteParcel(deliveryNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<String, Double>> getDeliveryPrices() {
        Map<String, Double> prices = deliveryPriceService.getDeliveryPrices();
        return ResponseEntity.ok(prices);
    }
}