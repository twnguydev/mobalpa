package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.service.PromotionService;
import com.mobalpa.api.filter.ProductFilter;
import com.mobalpa.api.dto.ProductWithCampaignDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.CategoryWithCampaignDTO;
import com.mobalpa.api.dto.SubcategoryWithCampaignDTO;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/catalogue")
@Tag(name = "Catalogue", description = "APIs for managing the catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @Autowired
  private PromotionService promotionService;

  @GetMapping("/categories")
  @Operation(summary = "Get all categories", description = "Fetches all categories.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                  "name": "Cuisines",
                  "description": "Ajoutez une touche de modernité à votre intérieur",
                  "uri": "cuisines",
                  "subcategories": [
                      {
                          "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                          "name": "Plan de travail",
                          "description": null,
                          "uri": "cuisines/plan-de-travail"
                      },
                      {
                          "uuid": "1b10f80b-1373-4091-bc1a-b1ebf7a982b6",
                          "name": "Table",
                          "description": null,
                          "uri": "cuisines/table"
                      }
                  ],
                  "images": [
                      {
                          "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                          "uri": "https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp",
                          "color": null
                      }
                  ]
              }
          ]
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """)))
  })
  public ResponseEntity<?> getAllCategories() {
    return ResponseEntity.ok(catalogueService.getAllCategories());
  }

  @GetMapping("/best-sellers")
  @Operation(summary = "Get best sellers", description = "Fetches the best selling products.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved best sellers.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                  "name": "Escapade",
                  "description": "This is an example product description.",
                  "price": 99.99,
                  "stock": 50,
                  "estimatedDelivery": "2024-08-01T10:00:00",
                  "category": {
                      "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                      "name": "Cuisines"
                  },
                  "subcategory": {
                      "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                      "name": "Plan de travail"
                  },
                  "brand": {
                      "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                      "name": "Archideco"
                  },
                  "colors": [
                      {
                          "uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0",
                          "name": "Rouge"
                      },
                      {
                          "uuid": "94ceb468-da88-442d-820e-446cd994144d",
                          "name": "Bleu"
                      }
                  ]
              }
          ]
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """)))
  })
  public ResponseEntity<?> getBestSellers() {
    return ResponseEntity.ok(catalogueService.getBestSellers());
  }

  @GetMapping("/coupons")
  @Operation(summary = "Get all coupons", description = "Fetches all coupons.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all coupons.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "id": 1,
                  "name": "Coupon 1",
                  "discountRate": 15.0,
                  "dateStart": "2024-08-01T10:00:00",
                  "dateEnd": "2024-08-31T23:59:59"
              }
          ]
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """)))
  })
  public ResponseEntity<?> getAllCoupons() {
    return ResponseEntity.ok(promotionService.getAllCoupons());
  }

  @GetMapping("/campaigns")
  @Operation(summary = "Get all campaigns", description = "Fetches all campaigns.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all campaigns.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "id": 2,
                  "type": "SUBCATEGORY",
                  "name": "Déstockage sur les Plans de travail",
                  "discountRate": 20.0,
                  "targetUuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                  "dateStart": "2024-08-22T11:54:59",
                  "dateEnd": "2024-08-27T23:59:59",
                  "createdAt": "2024-08-27T10:58:00"
              }
          ]
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """)))
  })
  public ResponseEntity<?> getAllCampaigns() {
    return ResponseEntity.ok(promotionService.getAllCampaigns());
  }

  @GetMapping("/products")
  @Operation(summary = "Get all products", description = "Fetches all products.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all products.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                  "name": "Escapade",
                  "description": "This is an example product description.",
                  "price": 99.99,
                  "stock": 50,
                  "estimatedDelivery": "2024-08-01T10:00:00",
                  "category": {
                      "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                      "name": "Cuisines"
                  },
                  "subcategory": {
                      "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                      "name": "Plan de travail"
                  },
                  "brand": {
                      "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                      "name": "Archideco"
                  },
                  "colors": [
                      {
                          "uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0",
                          "name": "Rouge"
                      },
                      {
                          "uuid": "94ceb468-da88-442d-820e-446cd994144d",
                          "name": "Bleu"
                      }
                  ]
              }
          ]
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """)))
  })
  public ResponseEntity<?> getAllProducts(
      @RequestParam(required = false) String color,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false) String brand) {
    try {
      ProductFilter productFilter = new ProductFilter();
      productFilter.setColorName(color);
      productFilter.setMinPrice(minPrice);
      productFilter.setMaxPrice(maxPrice);
      productFilter.setBrandName(brand);

      List<ProductDTO> products = catalogueService.getAllProducts(productFilter);
      return ResponseEntity.ok(products);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/products/{productUuid}/campaigns")
  @Operation(summary = "Get product campaigns", description = "Fetches the campaigns for a product.", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<?> getProductCampaigns(@PathVariable UUID productUuid) {
    return ResponseEntity.ok(promotionService.getProductCampaigns(productUuid));
  }

  @GetMapping("/products-with-campaigns")
  @Operation(summary = "Get all products with campaigns", description = "Fetches all products with campaigns.", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<?> getAllProductsWithCampaigns() {
    try {
      ProductFilter productFilter = new ProductFilter();
      List<ProductWithCampaignDTO> productsWithCampaigns = catalogueService.getAllProductsWithCampaign(productFilter);
      return ResponseEntity.ok(productsWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/subcategories-with-campaigns")
  @Operation(summary = "Get all subcategories with campaigns", description = "Fetches all subcategories with campaigns.", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<?> getAllSubcategoriesWithCampaigns() {
    try {
      List<SubcategoryWithCampaignDTO> subcategoriesWithCampaigns = catalogueService.getAllSubcategoriesWithCampaign();
      return ResponseEntity.ok(subcategoriesWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/categories-with-campaigns")
  @Operation(summary = "Get all categories with campaigns", description = "Fetches all categories with campaigns.", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<?> getAllCategoriesWithCampaigns() {
    try {
      List<CategoryWithCampaignDTO> categoriesWithCampaigns = catalogueService.getAllCategoriesWithCampaign();
      return ResponseEntity.ok(categoriesWithCampaigns);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/products/{productId}")
  @Operation(summary = "Get a product by UUID", description = "Fetches a product by its UUID.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the product.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
              "name": "Escapade",
              "description": "This is an example product description.",
              "price": 99.99,
              "stock": 50,
              "estimatedDelivery": "2024-08-01T10:00:00",
              "category": {
                  "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                  "name": "Cuisines"
              },
              "subcategory": {
                  "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                  "name": "Plan de travail"
              },
              "brand": {
                  "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                  "name": "Archideco"
              },
              "colors": [
                  {
                      "uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0",
                      "name": "Rouge"
                  },
                  {
                      "uuid": "94ceb468-da88-442d-820e-446cd994144d",
                      "name": "Bleu"
                  }
              ]
          }
          """))),
      @ApiResponse(responseCode = "403", description = "Unauthorized to access this resource.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Unauthorized",
              "message": "Invalid or missing authentication token."
          }
          """))),
      @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Not Found",
              "message": "No product found with the given UUID."
          }
          """)))
  })
  public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
    return ResponseEntity.ok(catalogueService.getProductById(productId));
  }

  @GetMapping("/categories/{categoryId}")
  @Operation(summary = "Get category by UUID", description = "Fetches a category by its unique identifier.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the category.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
              "name": "Cuisines",
              "description": "All items related to kitchen and dining.",
              "uri": "cuisines",
              "images": [
                  {
                      "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                      "uri": "https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp",
                      "color": null
                  }
              ]
          }
          """))),
      @ApiResponse(responseCode = "404", description = "Category not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Not Found",
              "message": "Category with the given UUID not found."
          }
          """)))
  })
  public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(catalogueService.getCategoryById(categoryId));
  }

  @GetMapping("/categories/{categoryId}/products")
  @Operation(summary = "Get products by category", description = "Fetches the products for a category.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved products for the category.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                  "name": "Escapade",
                  "description": "This is an example product description.",
                  "price": 99.99,
                  "stock": 50,
                  "estimatedDelivery": "2024-08-01T10:00:00",
                  "category": {
                      "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                      "name": "Cuisines",
                      "description": "Ajoutez une touche de modernité à votre intérieur",
                      "uri": "cuisines",
                      "images": [
                          {
                              "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                              "uri": "https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp",
                              "color": null
                          }
                      ]
                  },
                  "subcategory": {
                      "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                      "name": "Plan de travail",
                      "uri": "cuisines/plan-de-travail",
                      "description": null
                  },
                  "brand": {
                      "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                      "name": "Archideco"
                  },
                  "colors": [
                      {
                          "uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0",
                          "name": "Rouge"
                      },
                      {
                          "uuid": "94ceb468-da88-442d-820e-446cd994144d",
                          "name": "Bleu"
                      }
                  ]
              }
          ]
          """))),
      @ApiResponse(responseCode = "404", description = "No products found for the given category.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Not Found",
              "message": "No products found for the given category UUID."
          }
          """)))
  })
  public ResponseEntity<?> getProductsByCategory(@PathVariable UUID categoryId) {
    return ResponseEntity.ok(catalogueService.getProductsByCategoryId(categoryId));
  }

  @GetMapping("/subcategories")
  @Operation(summary = "Get all subcategories", description = "Fetches all subcategories.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all subcategories.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                  "name": "Plan de travail",
                  "description": "Countertops and work surfaces for kitchens.",
                  "uri": "cuisines/plan-de-travail"
              },
              {
                  "uuid": "2c20d91d-5a5a-4e28-8b3e-2b8a334ab6b7",
                  "name": "Électroménager",
                  "description": "Kitchen appliances and devices.",
                  "uri": "cuisines/electromenager"
              }
          ]
          """)))
  })
  public ResponseEntity<?> getAllSubcategories() {
    return ResponseEntity.ok(catalogueService.getAllSubcategories());
  }

  @GetMapping("/subcategories/{subcategoryId}/products")
  @Operation(summary = "Get products by subcategory", description = "Fetches the products for a subcategory.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved products for the subcategory.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          [
              {
                  "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                  "name": "Escapade",
                  "description": "This is an example product description.",
                  "price": 99.99,
                  "stock": 50,
                  "estimatedDelivery": "2024-08-01T10:00:00",
                  "category": {
                      "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                      "name": "Cuisines",
                      "description": "Ajoutez une touche de modernité à votre intérieur",
                      "uri": "cuisines",
                      "images": [
                          {
                              "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                              "uri": "https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp",
                              "color": null
                          }
                      ]
                  },
                  "subcategory": {
                      "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                      "name": "Plan de travail",
                      "description": null,
                      "uri": "cuisines/plan-de-travail"
                  },
                  "brand": {
                      "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                      "name": "Archideco"
                  },
                  "colors": [
                      {
                          "uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0",
                          "name": "Rouge"
                      },
                      {
                          "uuid": "94ceb468-da88-442d-820e-446cd994144d",
                          "name": "Bleu"
                      }
                  ]
              }
          ]
          """))),
      @ApiResponse(responseCode = "404", description = "No products found for the given subcategory.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Not Found",
              "message": "No products found for the given subcategory UUID."
          }
          """)))
  })
  public ResponseEntity<?> getProductsBySubcategory(
      @PathVariable UUID subcategoryId,
      @RequestParam(required = false) String color,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false) String brand) {
    try {
      ProductFilter productFilter = new ProductFilter();
      productFilter.setColorName(color);
      productFilter.setMinPrice(minPrice);
      productFilter.setMaxPrice(maxPrice);
      productFilter.setBrandName(brand);
      productFilter.setSubcategoryId(subcategoryId);

      List<ProductDTO> products = catalogueService.getAllProducts(productFilter);
      return ResponseEntity.ok(products);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/subcategories/{subcategoryId}")
  @Operation(summary = "Get subcategory by UUID", description = "Fetches a subcategory by its unique identifier.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the subcategory.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
              "name": "Plan de travail",
              "description": "Countertops and work surfaces for kitchens.",
              "uri": "cuisines/plan-de-travail"
          }
          """))),
      @ApiResponse(responseCode = "404", description = "Subcategory not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "error": "Not Found",
              "message": "Subcategory with the given UUID not found."
          }
          """)))
  })
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID subcategoryId) {
    return ResponseEntity.ok(catalogueService.getSubcategoryById(subcategoryId));
  }
}