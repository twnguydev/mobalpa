package com.mobalpa.delivery.service;

import com.mobalpa.delivery.dto.ParcelDTO;
import com.mobalpa.delivery.model.Parcel;
import com.mobalpa.delivery.model.ParcelItem;
import com.mobalpa.delivery.model.Shipment;
import com.mobalpa.delivery.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcelService {

    private static final Double LAW_MAX_WEIGHT_PER_PARCEL = 30.0;
    private static final Double LAW_MAX_WIDTH_PER_PARCEL = 100.0;
    private static final Double LAW_MAX_HEIGHT_PER_PARCEL = 100.0;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ShipmentService shipmentService;

    public Parcel createParcel(ParcelDTO parcelDTO) {
        Parcel parcel = new Parcel();
        parcel.setShippingMethodCheckoutName(parcelDTO.getShippingMethodCheckoutName());
        parcel.setSenderAddress(parcelDTO.getSenderAddress().toString());

        List<ParcelItem> parcelItems = parcelDTO.getParcelItems().stream().map(itemDTO -> {
            ParcelItem item = new ParcelItem();
            item.setDescription(itemDTO.getDescription());
            item.setProductId(itemDTO.getProductId());
            item.setProperties(itemDTO.getProperties());
            item.setQuantity(itemDTO.getQuantity());
            item.setValue(itemDTO.getValue());
            item.setWeight(itemDTO.getWeight());
            item.setWidth(itemDTO.getWidth());
            item.setHeight(itemDTO.getHeight());
            item.setParcel(parcel);
            return item;
        }).collect(Collectors.toList());

        parcel.setParcelItems(parcelItems);
        parcel.setTotalOrderValue(calculateTotalOrderValue(parcelItems));
        parcel.setTotalInsuredValue(calculateTotalOrderValue(parcelItems));
        parcel.setWeight(calculateTotalWeightParcel(parcelItems));
        parcel.setWidth(calculateTotalWidthParcel(parcelItems));
        parcel.setHeight(calculateTotalHeightParcel(parcelItems));
        parcel.setQuantity(calculateNumberOfParcelsNecessary(parcel));
        parcel.setRecipientAddress(parcelDTO.getRecipientAddress().toString());
        parcel.setRecipientName(parcelDTO.getRecipientName());
        parcel.setRecipientPhoneNumber(parcelDTO.getRecipientPhoneNumber());
        parcel.setRecipientEmail(parcelDTO.getRecipientEmail());
        parcel.setStatus("CREATED");
        parcel.setIsReturn(false);

        Shipment shipment = shipmentService.createShipment(parcelDTO);
        parcel.setShipment(shipment);

        return parcelRepository.save(parcel);
    }

    private Double calculateTotalOrderValue(List<ParcelItem> parcelItems) {
        if (parcelItems == null || parcelItems.isEmpty()) return 0.0;
        return parcelItems.stream().mapToDouble(item -> (item.getValue()) * item.getQuantity()).sum();
    }

    private Double calculateTotalWidthParcel(List<ParcelItem> parcelItems) {
        if (parcelItems == null || parcelItems.isEmpty()) return 0.0;
        return parcelItems.stream().mapToDouble(item -> (item.getWidth()) * item.getQuantity()).sum();
    }

    private Double calculateTotalHeightParcel(List<ParcelItem> parcelItems) {
        if (parcelItems == null || parcelItems.isEmpty()) return 0.0;
        return parcelItems.stream().mapToDouble(item -> (item.getHeight()) * item.getQuantity()).sum();
    }

    private Double calculateTotalWeightParcel(List<ParcelItem> parcelItems) {
        if (parcelItems == null || parcelItems.isEmpty()) return 0.0;
        return parcelItems.stream().mapToDouble(item -> (item.getWeight()) * item.getQuantity()).sum();
    }

    private Integer calculateNumberOfParcelsNecessary(Parcel parcel) {
        Double totalWeight = parcel.getWeight();
        Double totalWidth = parcel.getWidth();
        Double totalHeight = parcel.getHeight();

        Integer numberOfParcels = 1;

        if (totalWeight > LAW_MAX_WEIGHT_PER_PARCEL) {
            numberOfParcels = (int) Math.ceil(totalWeight / LAW_MAX_WEIGHT_PER_PARCEL);
        }

        if (totalWidth > LAW_MAX_WIDTH_PER_PARCEL) {
            numberOfParcels = (int) Math.ceil(totalWidth / LAW_MAX_WIDTH_PER_PARCEL);
        }

        if (totalHeight > LAW_MAX_HEIGHT_PER_PARCEL) {
            numberOfParcels = (int) Math.ceil(totalHeight / LAW_MAX_HEIGHT_PER_PARCEL);
        }

        return numberOfParcels;
    }

    public Parcel getParcelByDeliveryNumber(String deliveryNumber) {
        return parcelRepository.findByShipmentDeliveryNumber(deliveryNumber)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));
    }

    public Parcel updateParcelStatus(String deliveryNumber, String status) {
        Parcel parcel = getParcelByDeliveryNumber(deliveryNumber);
        parcel.setStatus(status);
        return parcelRepository.save(parcel);
    }

    public void deleteParcel(String deliveryNumber) {
        Parcel parcel = getParcelByDeliveryNumber(deliveryNumber);
        parcelRepository.delete(parcel);
    }
}