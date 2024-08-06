package com.mobalpa.delivery.controller;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.model.Parcel;
import com.mobalpa.delivery.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private ParcelService parcelService;

    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody ParcelDTO parcelDTO) {
        Parcel createdParcel = parcelService.createParcel(parcelDTO);
        return ResponseEntity.status(201).body(createdParcel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcel> getParcelById(@PathVariable UUID id) {
        Parcel parcel = parcelService.getParcelById(id);
        return ResponseEntity.ok(parcel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Parcel> updateParcelStatus(@PathVariable UUID id, @RequestBody String status) {
        Parcel updatedParcel = parcelService.updateParcelStatus(id, status);
        return ResponseEntity.ok(updatedParcel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcel(@PathVariable UUID id) {
        parcelService.deleteParcel(id);
        return ResponseEntity.noContent().build();
    }
}

// package com.mobalpa.delivery.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.HttpStatus;

// import com.mobalpa.delivery.dto.DeliveryDTO;
// import com.mobalpa.delivery.dto.DeliveryPriceRequestDTO;
// import com.mobalpa.delivery.dto.TrackingDetailsDTO;
// import com.mobalpa.delivery.model.Delivery;
// import com.mobalpa.delivery.service.DeliveryService;

// import java.util.Map;
// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/deliveries")
// public class DeliveryController {

// @Autowired
// private DeliveryService deliveryService;

// @PostMapping
// public ResponseEntity<Delivery> createDelivery(@RequestBody DeliveryDTO
// deliveryDTO) {
// Delivery delivery = deliveryService.createDelivery(deliveryDTO);
// return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
// }

// @GetMapping("/{id}")
// public ResponseEntity<Delivery> getDelivery(@PathVariable UUID id) {
// Delivery delivery = deliveryService.getDeliveryById(id)
// .orElseThrow(() -> new RuntimeException("Delivery not found"));
// return ResponseEntity.ok(delivery);
// }

// @PatchMapping("/{id}")
// public ResponseEntity<Delivery> updateDeliveryStatus(@PathVariable UUID id,
// @RequestBody String status) {
// Delivery.StatusType statusType = Delivery.StatusType.valueOf(status);
// Delivery delivery = deliveryService.updateDeliveryStatus(id, statusType);
// return ResponseEntity.ok(delivery);
// }

// @GetMapping("/{id}/track")
// public ResponseEntity<List<TrackingDetailsDTO>>
// getTrackingDetails(@PathVariable UUID id) {
// List<TrackingDetailsDTO> trackingDetails =
// deliveryService.getTrackingDetails(id);
// return ResponseEntity.ok(trackingDetails);
// }

// @PostMapping("/prices")
// public ResponseEntity<?> getDeliveryPrices(@RequestBody
// DeliveryPriceRequestDTO requestDTO) {
// try {
// Map<String, Double> prices = deliveryService.getDeliveryPrices(requestDTO);
// return ResponseEntity.ok(prices);
// } catch (RuntimeException e) {
// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
// } catch (Exception e) {
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
// }
// }
// }