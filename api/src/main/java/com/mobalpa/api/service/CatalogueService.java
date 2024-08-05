package com.mobalpa.api.service;

import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        CategoryDTO[] categories = restTemplate.getForObject(this.baseUrl + "/categories", CategoryDTO[].class);
        return Arrays.asList(categories);
    }

    public List<ProductDTO> getBestSellers() {
        ProductDTO[] products = restTemplate.getForObject(this.baseUrl + "/best-sellers", ProductDTO[].class);
        return Arrays.asList(products);
    }

    public String getStoreById(UUID id) {
        return restTemplate.getForObject(this.baseUrl + "/store/" + id, String.class);
    }

    public List<ProductDTO> getAllProducts() {
        ProductDTO[] products = restTemplate.getForObject(this.baseUrl + "/products", ProductDTO[].class);
        return Arrays.asList(products);
    }

    public ProductDTO getProductById(UUID id) {
        return restTemplate.getForObject(this.baseUrl + "/products/" + id, ProductDTO.class);
    }

    public CategoryDTO getCategoryById(UUID id) {
        return restTemplate.getForObject(this.baseUrl + "/categories/" + id, CategoryDTO.class);
    }

    public SubcategoryDTO getSubcategoryById(UUID id) {
        return restTemplate.getForObject(this.baseUrl + "/subcategories/" + id, SubcategoryDTO.class);
    }

    public List<ProductDTO> getProductsByCategoryId(UUID categoryId) {
        ProductDTO[] products = restTemplate.getForObject(this.baseUrl + "/products/" + categoryId, ProductDTO[].class);
        return Arrays.asList(products);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        return restTemplate.postForObject(this.baseUrl + "/products", productDTO, ProductDTO.class);
    }

    public void updateProduct(UUID id, ProductDTO productDTO) {
        restTemplate.patchForObject(this.baseUrl + "/products/" + id, productDTO, Void.class);
    }

    public void deleteProduct(UUID id) {
        restTemplate.delete(this.baseUrl + "/products/" + id);
    }

    public List<SubcategoryDTO> getAllProductCategories() {
        SubcategoryDTO[] categories = restTemplate.getForObject(this.baseUrl + "/products/categories", SubcategoryDTO[].class);
        return Arrays.asList(categories);
    }

    public SubcategoryDTO createProductCategory(SubcategoryDTO categoryDTO) {
        return restTemplate.postForObject(this.baseUrl + "/products/categories", categoryDTO, SubcategoryDTO.class);
    }

    public void updateProductCategory(UUID id, SubcategoryDTO categoryDTO) {
        restTemplate.patchForObject(this.baseUrl + "/products/categories/" + id, categoryDTO, Void.class);
    }

    public void deleteProductCategory(UUID id) {
        restTemplate.delete(this.baseUrl + "/products/categories/" + id);
    }
}