package com.mobalpa.api.service;

import com.mobalpa.api.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CatalogueService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8081/api/catalogue";

    public List<CatalogueCategoryDTO> getAllCategories() {
        CatalogueCategoryDTO[] categories = restTemplate.getForObject(BASE_URL + "/categories", CatalogueCategoryDTO[].class);
        return Arrays.asList(categories);
    }

    public List<CatalogueProductDTO> getBestSellers() {
        CatalogueProductDTO[] products = restTemplate.getForObject(BASE_URL + "/best-sellers", CatalogueProductDTO[].class);
        return Arrays.asList(products);
    }

    public String getStoreById(UUID id) {
        return restTemplate.getForObject(BASE_URL + "/store/" + id, String.class);
    }

    public List<CatalogueProductDTO> getAllProducts() {
        CatalogueProductDTO[] products = restTemplate.getForObject(BASE_URL + "/products", CatalogueProductDTO[].class);
        return Arrays.asList(products);
    }

    public List<CatalogueProductDTO> getProductsByCategoryId(UUID categoryId) {
        CatalogueProductDTO[] products = restTemplate.getForObject(BASE_URL + "/products/" + categoryId, CatalogueProductDTO[].class);
        return Arrays.asList(products);
    }

    public CatalogueProductDTO createProduct(CatalogueProductDTO productDTO) {
        return restTemplate.postForObject(BASE_URL + "/products", productDTO, CatalogueProductDTO.class);
    }

    public void updateProduct(UUID id, CatalogueProductDTO productDTO) {
        restTemplate.patchForObject(BASE_URL + "/products/" + id, productDTO, Void.class);
    }

    public void deleteProduct(UUID id) {
        restTemplate.delete(BASE_URL + "/products/" + id);
    }

    public List<CatalogueSubcategoryDTO> getAllProductCategories() {
        CatalogueSubcategoryDTO[] categories = restTemplate.getForObject(BASE_URL + "/products/categories", CatalogueSubcategoryDTO[].class);
        return Arrays.asList(categories);
    }

    public CatalogueSubcategoryDTO createProductCategory(CatalogueSubcategoryDTO categoryDTO) {
        return restTemplate.postForObject(BASE_URL + "/products/categories", categoryDTO, CatalogueSubcategoryDTO.class);
    }

    public void updateProductCategory(UUID id, CatalogueSubcategoryDTO categoryDTO) {
        restTemplate.patchForObject(BASE_URL + "/products/categories/" + id, categoryDTO, Void.class);
    }

    public void deleteProductCategory(UUID id) {
        restTemplate.delete(BASE_URL + "/products/categories/" + id);
    }
}