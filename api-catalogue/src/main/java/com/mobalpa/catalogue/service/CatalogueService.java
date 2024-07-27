package com.mobalpa.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.mobalpa.catalogue.repository.ProductRepository;
import com.mobalpa.catalogue.repository.StoreRepository;
import com.mobalpa.catalogue.model.Store;
import com.mobalpa.catalogue.model.Product;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogueService {

    private static final String URL_ECOMMERCE_API = "http://localhost:8080/api/orders";

    @Value("${api.key}")
    private String X_API_KEY;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(UUID id) {
        return storeRepository.findById(id);
    }

    public List<Product> getBestSellers() {
        List<Map<String, Object>> orders = fetchOrdersFromEcommerceApi();
        Map<UUID, Integer> productSalesCount = new HashMap<>();

        for (Map<String, Object> order : orders) {
            List<Map<String, Object>> items = getItems(order.get("items"));
            for (Map<String, Object> item : items) {
                UUID productId = UUID.fromString((String) item.get("productId"));
                int quantity = (int) item.get("quantity");
                productSalesCount.put(productId,
                    productSalesCount.getOrDefault(productId, 0) + quantity);
            }
        }

        List<UUID> bestSellingProductIds = productSalesCount.entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .map(Map.Entry::getKey)
            .limit(10)
            .collect(Collectors.toList());

        return productRepository.findProductsByIds(bestSellingProductIds);
    }

    private List<Map<String, Object>> fetchOrdersFromEcommerceApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", X_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            URL_ECOMMERCE_API, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getItems(Object items) {
        if (items instanceof List) {
            return (List<Map<String, Object>>) items;
        } else {
            return Collections.emptyList();
        }
    }
}