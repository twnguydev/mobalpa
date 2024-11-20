package com.mobalpa.api.controller;

import com.mobalpa.api.dto.ProductWithCampaignDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.CategoryDTO;
import com.mobalpa.api.dto.catalogue.SubcategoryDTO;
import com.mobalpa.api.dto.CategoryWithCampaignDTO;
import com.mobalpa.api.dto.SubcategoryWithCampaignDTO;
import com.mobalpa.api.filter.ProductFilter;
import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.service.PromotionService;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CatalogueControllerTest {

    @Mock
    private CatalogueService catalogueService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private CatalogueController catalogueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        CategoryDTO category = new CategoryDTO();
        category.setName("Category1");

        when(catalogueService.getAllCategories()).thenReturn(List.of(category));

        ResponseEntity<?> response = catalogueController.getAllCategories();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(category), response.getBody());
    }

    @Test
    void testGetBestSellers() {
        ProductDTO bestSeller = new ProductDTO();
        bestSeller.setName("Product1");
    
        when(catalogueService.getBestSellers()).thenReturn(List.of(bestSeller));

        ResponseEntity<?> response = catalogueController.getBestSellers();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<ProductDTO> responseBody = (List<ProductDTO>) response.getBody();
        assertEquals(1, responseBody.size());
        assertEquals(bestSeller, responseBody.get(0));
    }    

    @Test
    void testGetAllCoupons() {
        CouponCode coupon = new CouponCode();
        coupon.setName("Coupon1");

        when(promotionService.getAllCoupons()).thenReturn(List.of(coupon));

        ResponseEntity<?> response = catalogueController.getAllCoupons();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(coupon), response.getBody());
    }

    @Test
    void testGetAllCampaigns() {
        Campaign campaign = new Campaign();
        campaign.setName("Campaign1");

        when(promotionService.getAllCampaigns()).thenReturn(List.of(campaign));

        ResponseEntity<?> response = catalogueController.getAllCampaigns();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(campaign), response.getBody());
    }

    @Test
    void testGetAllProductsSuccess() {
        List<ProductDTO> products = List.of(new ProductDTO());
        when(catalogueService.getAllProducts(any(ProductFilter.class))).thenReturn(products);

        ResponseEntity<?> response = catalogueController.getAllProducts(null, null, null, null);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void testGetAllProductsFailure() {
        when(catalogueService.getAllProducts(any(ProductFilter.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = catalogueController.getAllProducts(null, null, null, null);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testGetProductCampaigns() {
        UUID productUuid = UUID.randomUUID();
        Campaign campaigns = new Campaign();
        campaigns.setName("Campaign1");

        when(promotionService.getProductCampaigns(productUuid)).thenReturn(List.of(campaigns));

        ResponseEntity<?> response = catalogueController.getProductCampaigns(productUuid);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(campaigns), response.getBody());
    }

    @Test
    void testGetAllProductsWithCampaignsSuccess() {
        List<ProductWithCampaignDTO> productsWithCampaigns = List.of(new ProductWithCampaignDTO());
        when(catalogueService.getAllProductsWithCampaign(any(ProductFilter.class))).thenReturn(productsWithCampaigns);

        ResponseEntity<?> response = catalogueController.getAllProductsWithCampaigns();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productsWithCampaigns, response.getBody());
    }

    @Test
    void testGetAllProductsWithCampaignsFailure() {
        when(catalogueService.getAllProductsWithCampaign(any(ProductFilter.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = catalogueController.getAllProductsWithCampaigns();
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testGetAllSubcategoriesWithCampaignsSuccess() {
        List<SubcategoryWithCampaignDTO> subcategoriesWithCampaigns = List.of(new SubcategoryWithCampaignDTO());
        when(catalogueService.getAllSubcategoriesWithCampaign()).thenReturn(subcategoriesWithCampaigns);

        ResponseEntity<?> response = catalogueController.getAllSubcategoriesWithCampaigns();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subcategoriesWithCampaigns, response.getBody());
    }

    @Test
    void testGetAllSubcategoriesWithCampaignsFailure() {
        when(catalogueService.getAllSubcategoriesWithCampaign()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = catalogueController.getAllSubcategoriesWithCampaigns();
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testGetAllCategoriesWithCampaignsSuccess() {
        List<CategoryWithCampaignDTO> categoriesWithCampaigns = List.of(new CategoryWithCampaignDTO());
        when(catalogueService.getAllCategoriesWithCampaign()).thenReturn(categoriesWithCampaigns);

        ResponseEntity<?> response = catalogueController.getAllCategoriesWithCampaigns();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriesWithCampaigns, response.getBody());
    }

    @Test
    void testGetAllCategoriesWithCampaignsFailure() {
        when(catalogueService.getAllCategoriesWithCampaign()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = catalogueController.getAllCategoriesWithCampaigns();
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testGetProductById() {
        UUID productId = UUID.randomUUID();
        ProductDTO product = new ProductDTO();
        when(catalogueService.getProductById(productId)).thenReturn(product);

        ResponseEntity<?> response = catalogueController.getProductById(productId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testGetCategoryById() {
        UUID categoryId = UUID.randomUUID();
        CategoryDTO category = new CategoryDTO();
        when(catalogueService.getCategoryById(categoryId)).thenReturn(category);

        ResponseEntity<?> response = catalogueController.getCategoryById(categoryId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void testGetProductsByCategory() {
        UUID categoryId = UUID.randomUUID();
        List<ProductDTO> products = List.of(new ProductDTO());
        when(catalogueService.getProductsByCategoryId(categoryId)).thenReturn(products);

        ResponseEntity<?> response = catalogueController.getProductsByCategory(categoryId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void testGetAllSubcategories() {
        List<SubcategoryDTO> subcategories = List.of(new SubcategoryDTO());
        when(catalogueService.getAllSubcategories()).thenReturn(subcategories);

        ResponseEntity<?> response = catalogueController.getAllSubcategories();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subcategories, response.getBody());
    }

    @Test
    void testGetProductsBySubcategorySuccess() {
        UUID subcategoryId = UUID.randomUUID();
        List<ProductDTO> products = List.of(new ProductDTO());
        when(catalogueService.getAllProducts(any(ProductFilter.class))).thenReturn(products);

        ResponseEntity<?> response = catalogueController.getProductsBySubcategory(subcategoryId, null, null, null, null);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void testGetProductsBySubcategoryFailure() {
        UUID subcategoryId = UUID.randomUUID();
        when(catalogueService.getAllProducts(any(ProductFilter.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = catalogueController.getProductsBySubcategory(subcategoryId, null, null, null, null);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testGetSubcategoryById() {
        UUID subcategoryId = UUID.randomUUID();
        SubcategoryDTO subcategory = new SubcategoryDTO();
        when(catalogueService.getSubcategoryById(subcategoryId)).thenReturn(subcategory);

        ResponseEntity<?> response = catalogueController.getSubcategoryById(subcategoryId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subcategory, response.getBody());
    }
}