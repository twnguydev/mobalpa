package com.mobalpa.api.service;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CatalogueService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${catalogue.base-url}")
    private String baseUrl;

    @Value("${catalogue.api-key}")
    private String apiKey;

    public List<CategoryDTO> getAllCategories() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<CategoryDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/categories", HttpMethod.GET, request, CategoryDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public List<ProductDTO> getBestSellers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/best-sellers", HttpMethod.GET, request, ProductDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public String getStoreById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                this.baseUrl + "/store/" + id, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    public List<ProductDTO> getAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/products", HttpMethod.GET, request, ProductDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public ProductDTO getProductById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProductDTO> response = restTemplate.exchange(
                this.baseUrl + "/products/" + id.toString(), HttpMethod.GET, request, ProductDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Product with UUID " + id + " not found");
            } else {
                throw new RuntimeException("Error fetching product: " + e.getMessage());
            }
        }
    }

    public CategoryDTO getCategoryById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<CategoryDTO> response = restTemplate.exchange(
                this.baseUrl + "/categories/" + id, HttpMethod.GET, request, CategoryDTO.class);
        return response.getBody();
    }

    public SubcategoryDTO getSubcategoryById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<SubcategoryDTO> response = restTemplate.exchange(
                this.baseUrl + "/subcategories/" + id, HttpMethod.GET, request, SubcategoryDTO.class);
        return response.getBody();
    }

    public List<ProductDTO> getProductsByCategoryId(UUID categoryId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/categories/" + categoryId + "/products", HttpMethod.GET, request, ProductDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDTO, headers);

        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                this.baseUrl + "/products", HttpMethod.POST, request, ProductDTO.class);
        return response.getBody();
    }

    public void updateProduct(UUID id, ProductDTO productDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDTO, headers);

        restTemplate.exchange(this.baseUrl + "/products/" + id, HttpMethod.PATCH, request, Void.class);
    }

    public void deleteProduct(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        restTemplate.exchange(this.baseUrl + "/products/" + id, HttpMethod.DELETE, request, Void.class);
    }

    public List<SubcategoryDTO> getAllProductCategories() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<SubcategoryDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/products/categories", HttpMethod.GET, request, SubcategoryDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public SubcategoryDTO createProductCategory(SubcategoryDTO categoryDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubcategoryDTO> request = new HttpEntity<>(categoryDTO, headers);

        ResponseEntity<SubcategoryDTO> response = restTemplate.exchange(
                this.baseUrl + "/products/categories", HttpMethod.POST, request, SubcategoryDTO.class);
        return response.getBody();
    }

    public void updateProductCategory(UUID id, SubcategoryDTO categoryDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubcategoryDTO> request = new HttpEntity<>(categoryDTO, headers);

        restTemplate.exchange(this.baseUrl + "/products/categories/" + id, HttpMethod.PATCH, request, Void.class);
    }

    public void deleteProductCategory(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        restTemplate.exchange(this.baseUrl + "/products/categories/" + id, HttpMethod.DELETE, request, Void.class);
    }
}