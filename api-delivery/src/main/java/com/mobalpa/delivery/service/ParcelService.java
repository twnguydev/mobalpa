package com.mobalpa.delivery.service;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.model.Parcel;
import com.mobalpa.delivery.model.ParcelItem;
import com.mobalpa.delivery.model.Shipment;
import com.mobalpa.delivery.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ShipmentService shipmentService;

    public Parcel createParcel(ParcelDTO parcelDTO) {
        Parcel parcel = new Parcel();
        parcel.setName(parcelDTO.getName());
        parcel.setCompanyName(parcelDTO.getCompanyName());
        parcel.setEmail(parcelDTO.getEmail());
        parcel.setTelephone(parcelDTO.getTelephone());
        parcel.setAddress(parcelDTO.getAddress());
        parcel.setHouseNumber(parcelDTO.getHouseNumber());
        parcel.setAddress2(parcelDTO.getAddress2());
        parcel.setCity(parcelDTO.getCity());
        parcel.setCountry(parcelDTO.getCountry());
        parcel.setPostalCode(parcelDTO.getPostalCode());
        parcel.setCountryState(parcelDTO.getCountryState());
        parcel.setToServicePoint(parcelDTO.getToServicePoint().toString());
        parcel.setToPostNumber(parcelDTO.getToPostNumber().toString());
        parcel.setCustomsInvoiceNr(parcelDTO.getCustomsInvoiceNr());
        parcel.setCustomsShipmentType(parcelDTO.getCustomsShipmentType());
        parcel.setWeight(parcelDTO.getWeight());
        parcel.setLength(parcelDTO.getLength());
        parcel.setWidth(parcelDTO.getWidth());
        parcel.setHeight(parcelDTO.getHeight());
        parcel.setTotalOrderValue(parcelDTO.getTotalOrderValue());
        parcel.setTotalOrderValueCurrency(parcelDTO.getTotalOrderValueCurrency());
        parcel.setShippingMethodCheckoutName(parcelDTO.getShippingMethodCheckoutName());
        parcel.setSenderAddress(parcelDTO.getSenderAddress().toString());
        parcel.setQuantity(parcelDTO.getQuantity());
        parcel.setTotalInsuredValue(parcelDTO.getTotalInsuredValue());
        parcel.setIsReturn(parcelDTO.getIsReturn());
        parcel.setRequestLabel(parcelDTO.getRequestLabel());
        parcel.setApplyShippingRules(parcelDTO.getApplyShippingRules());
        parcel.setRequestLabelAsync(parcelDTO.getRequestLabelAsync());

        Shipment shipment = shipmentService.createShipmentFromParcel(parcelDTO);
        parcel.setShipment(shipment);

        List<ParcelItem> parcelItems = parcelDTO.getParcelItems().stream().map(itemDTO -> {
            ParcelItem item = new ParcelItem();
            item.setDescription(itemDTO.getDescription());
            item.setHsCode(itemDTO.getHsCode());
            item.setOriginCountry(itemDTO.getOriginCountry());
            item.setProductId(itemDTO.getProductId());
            item.setProperties(itemDTO.getProperties());
            item.setQuantity(itemDTO.getQuantity());
            item.setSku(itemDTO.getSku());
            item.setValue(itemDTO.getValue());
            item.setWeight(itemDTO.getWeight());
            item.setParcel(parcel);
            return item;
        }).collect(Collectors.toList());

        parcel.setParcelItems(parcelItems);

        return parcelRepository.save(parcel);
    }

    public void updateShipment(Parcel parcel, Shipment shipment) {
        parcel.setShipment(shipment);
    }

    public Parcel getParcelById(UUID id) {
        return parcelRepository.findById(id).orElseThrow(() -> new RuntimeException("Parcel not found"));
    }

    public Parcel updateParcelStatus(UUID id, String status) {
        Parcel parcel = getParcelById(id);
        parcel.setStatus(status);
        return parcelRepository.save(parcel);
    }

    public void deleteParcel(UUID id) {
        parcelRepository.deleteById(id);
    }
}