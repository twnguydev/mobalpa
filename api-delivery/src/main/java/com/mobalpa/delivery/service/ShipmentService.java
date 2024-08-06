package com.mobalpa.delivery.service;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.dto.ShipmentDTO;
import com.mobalpa.delivery.model.Shipment;
import com.mobalpa.delivery.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public Shipment createShipmentFromParcel(ParcelDTO parcelDTO) {
        Shipment shipment = new Shipment();
        shipment.setName(parcelDTO.getShippingMethodCheckoutName());
        return shipment;
    }

    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    public Shipment updateShipment(Long id, ShipmentDTO shipmentDTO) {
        Shipment shipment = getShipmentById(id);
        shipment.setName(shipmentDTO.getName());
        // Update other fields if necessary
        return shipmentRepository.save(shipment);
    }

    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }
}