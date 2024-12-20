package com.mobalpa.api.service;

import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.repository.CampaignRepository;
import com.mobalpa.api.dto.CategoryWithCampaignDTO;
import com.mobalpa.api.dto.ProductWithCampaignDTO;
import com.mobalpa.api.dto.SubcategoryWithCampaignDTO;
import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.filter.ProductFilter;

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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class CatalogueService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${catalogue.base-url}")
    private String baseUrl;

    @Value("${catalogue.api-key}")
    private String apiKey;

    @Autowired
    private CampaignRepository campaignRepository;

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

    public List<ProductDTO> getAllProducts(ProductFilter productFilter) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/products", HttpMethod.GET, request, ProductDTO[].class);

        List<ProductDTO> products = Arrays.asList(response.getBody());

        return products.stream().filter(product -> {
            boolean matches = true;
            if (productFilter.getMaxPrice() != null) {
                matches = matches && product.getPrice() <= productFilter.getMaxPrice();
            }
            if (productFilter.getMinPrice() != null) {
                matches = matches && product.getPrice() >= productFilter.getMinPrice();
            }
            if (productFilter.getBrandName() != null) {
                matches = matches && product.getBrand().getName().equalsIgnoreCase(productFilter.getBrandName());
            }
            if (productFilter.getColorName() != null) {
                matches = matches && product.getColors().stream()
                        .anyMatch(color -> color.getName().equalsIgnoreCase(productFilter.getColorName()));
            }
            if (productFilter.getSubcategoryId() != null) {
                matches = matches && product.getSubcategory().getUuid().equals(productFilter.getSubcategoryId());
            }
            return matches;
        }).collect(Collectors.toList());
    }

    public List<ProductWithCampaignDTO> getAllProductsWithCampaign(ProductFilter productFilter) {
        List<ProductDTO> allProducts = getAllProducts(productFilter);
        List<Campaign> campaigns = campaignRepository.findAll();

        ZoneId zoneId = ZoneId.of("Europe/Paris");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        System.out.println("Current Time: " + now);

        List<Campaign> activeCampaigns = campaigns.stream()
                .filter(campaign -> {
                    ZonedDateTime couponStart = campaign.getDateStart().atZone(zoneId);
                    ZonedDateTime couponEnd = campaign.getDateEnd().atZone(zoneId);
                    return couponStart.isBefore(now) && couponEnd.isAfter(now);
                })
                .collect(Collectors.toList());

        return allProducts.stream()
                .map(product -> {
                    List<Campaign> activeCampaignsForProduct = activeCampaigns.stream()
                            .filter(campaign -> campaign.getTargetUuid().equals(product.getUuid())
                                    || (campaign.getType() == Campaign.Type.SUBCATEGORY
                                            && campaign.getTargetUuid().equals(product.getSubcategory().getUuid())))
                            .collect(Collectors.toList());

                    if (!activeCampaignsForProduct.isEmpty()) {
                        ProductWithCampaignDTO productWithCampaign = new ProductWithCampaignDTO();
                        productWithCampaign.setProduct(product);
                        productWithCampaign.setCampaigns(activeCampaignsForProduct);
                        return productWithCampaign;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<CategoryWithCampaignDTO> getAllCategoriesWithCampaign() {
        List<CategoryDTO> allCategories = getAllCategories();
        List<Campaign> campaigns = campaignRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        return allCategories.stream()
                .map(category -> {
                    List<Campaign> activeCampaigns = campaigns.stream()
                            .filter(campaign -> campaign.getType() == Campaign.Type.CATEGORY &&
                                    campaign.getTargetUuid().equals(category.getUuid()) &&
                                    campaign.getDateStart().isBefore(now) &&
                                    campaign.getDateEnd().isAfter(now))
                            .collect(Collectors.toList());

                    if (!activeCampaigns.isEmpty()) {
                        CategoryWithCampaignDTO categoryWithCampaign = new CategoryWithCampaignDTO();
                        categoryWithCampaign.setCategory(category);
                        categoryWithCampaign.setCampaigns(activeCampaigns);
                        return categoryWithCampaign;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<SubcategoryWithCampaignDTO> getAllSubcategoriesWithCampaign() {
        List<SubcategoryDTO> allSubcategories = getAllSubcategories();
        List<Campaign> campaigns = campaignRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        return allSubcategories.stream()
                .map(subcategory -> {
                    List<Campaign> activeCampaigns = campaigns.stream()
                            .filter(campaign -> campaign.getType() == Campaign.Type.SUBCATEGORY &&
                                    campaign.getTargetUuid().equals(subcategory.getUuid()) &&
                                    campaign.getDateStart().isBefore(now) &&
                                    campaign.getDateEnd().isAfter(now))
                            .collect(Collectors.toList());

                    if (!activeCampaigns.isEmpty()) {
                        SubcategoryWithCampaignDTO subcategoryWithCampaign = new SubcategoryWithCampaignDTO();
                        subcategoryWithCampaign.setSubcategory(subcategory);
                        subcategoryWithCampaign.setCampaigns(activeCampaigns);
                        return subcategoryWithCampaign;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    public List<ProductDTO> getProductsBySubcategoryId(UUID subcategoryId, ProductFilter productFilter) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/subcategories/" + subcategoryId + "/products",
                HttpMethod.GET,
                request,
                ProductDTO[].class);

        List<ProductDTO> products = Arrays.asList(response.getBody());

        return products.stream().filter(product -> {
            boolean matches = true;
            if (productFilter.getMaxPrice() != null) {
                matches = matches && product.getPrice() <= productFilter.getMaxPrice();
            }
            if (productFilter.getMinPrice() != null) {
                matches = matches && product.getPrice() >= productFilter.getMinPrice();
            }
            if (productFilter.getBrandName() != null) {
                matches = matches && product.getBrand().getName().equalsIgnoreCase(productFilter.getBrandName());
            }
            if (productFilter.getColorName() != null) {
                matches = matches && product.getColors().stream()
                        .anyMatch(color -> color.getName().equalsIgnoreCase(productFilter.getColorName()));
            }
            return matches;
        }).collect(Collectors.toList());
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

    public List<SubcategoryDTO> getAllSubcategories() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", this.apiKey);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<SubcategoryDTO[]> response = restTemplate.exchange(
                this.baseUrl + "/subcategories", HttpMethod.GET, request, SubcategoryDTO[].class);
        return Arrays.asList(response.getBody());
    }
}