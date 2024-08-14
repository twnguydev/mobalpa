package com.mobalpa.delivery.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DepotService {

    private static final Map<String, DeliveryInfo> DELIVERY_PRICES = new HashMap<>();

    static {
        DELIVERY_PRICES.put("Chronopost", new DeliveryInfo(5.60, "Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris"));
        DELIVERY_PRICES.put("La Poste", new DeliveryInfo(4.20, "La Poste Depot, 25 Avenue de la République, 75011 Paris"));
        DELIVERY_PRICES.put("Mobalpa Centrale", new DeliveryInfo(0.0, "Mobalpa Centrale, 10 Rue du Commerce, 69002 Lyon"));
    }

    public Map<String, DeliveryInfo> getDeliveryPrices() {
        return DELIVERY_PRICES;
    }

    public boolean isDeliveryMethodAvailable(String method) {
        return DELIVERY_PRICES.containsKey(method);
    }

    public DeliveryInfo getDeliveryPrice(String method) {
        return DELIVERY_PRICES.get(method);
    }

    public static class DeliveryInfo {
        private double price;
        private String address;

        public DeliveryInfo(double price, String address) {
            this.price = price;
            this.address = address;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}