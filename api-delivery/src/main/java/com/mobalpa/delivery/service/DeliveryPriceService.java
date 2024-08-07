package com.mobalpa.delivery.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeliveryPriceService {

    private static final Map<String, Double> DELIVERY_PRICES = new HashMap<>();

    static {
        DELIVERY_PRICES.put("Chronopost", 5.60);
        DELIVERY_PRICES.put("La Poste", 4.20);
        DELIVERY_PRICES.put("Mobalpa Centrale", 0.0);
    }

    public Map<String, Double> getDeliveryPrices() {
        return DELIVERY_PRICES;
    }

    public boolean isDeliveryMethodAvailable(String method) {
        return DELIVERY_PRICES.containsKey(method);
    }
}