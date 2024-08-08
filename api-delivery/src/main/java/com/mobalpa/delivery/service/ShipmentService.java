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

    public Shipment createShipment(ParcelDTO parcelDTO) {
        Shipment shipment = new Shipment(parcelDTO);
        shipment.setName(parcelDTO.getShippingMethodCheckoutName());
        shipment.setOrderUuid(parcelDTO.getOrderUuid());
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentByDeliveryNumber(String number) {
        return shipmentRepository.findByDeliveryNumber(number).orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    public Shipment updateShipment(String number, ShipmentDTO shipmentDTO) {
        Shipment shipment = getShipmentByDeliveryNumber(number);
        shipment.setName(shipmentDTO.getName());
        return shipmentRepository.save(shipment);
    }

    public void deleteShipment(String number) {
        shipmentRepository.deleteByDeliveryNumber(number);
    }
}