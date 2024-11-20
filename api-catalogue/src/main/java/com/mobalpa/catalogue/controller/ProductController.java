package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.model.Store;
import com.mobalpa.catalogue.service.ProductService;
import com.mobalpa.catalogue.service.CatalogueService;
import com.mobalpa.catalogue.mapper.Mapper;
import com.mobalpa.catalogue.dto.ProductDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.catalogue.filter.ProductFilter;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/catalogue/products")
@Tag(name = "Product", description = "APIs for managing products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CatalogueService catalogueService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Fetches all products.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
                    +
                    "  {\n" +
                    "    \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "    \"name\": \"Escapade\",\n" +
                    "    \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
                    "    \"description\": \"This is an example product description.\",\n" +
                    "    \"price\": 99.99,\n" +
                    "    \"stock\": 50,\n" +
                    "    \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "    \"weight\": 1.5,\n" +
                    "    \"height\": 10.0,\n" +
                    "    \"width\": 20.0,\n" +
                    "    \"createdAt\": \"2024-08-05T16:24:23\",\n" +
                    "    \"category\": {\n" +
                    "      \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "      \"name\": \"Cuisines\",\n" +
                    "      \"uri\": \"cuisines\"\n" +
                    "    },\n" +
                    "    \"subcategory\": {\n" +
                    "      \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "      \"name\": \"Plan de travail\",\n" +
                    "      \"uri\": \"cuisines/plan-de-travail\"\n" +
                    "    },\n" +
                    "    \"brand\": {\n" +
                    "      \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "      \"name\": \"Archideco\",\n" +
                    "      \"uri\": null\n" +
                    "    },\n" +
                    "    \"colors\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Rouge\",\n" +
                    "        \"uri\": null\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "        \"name\": \"Bleu\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"images\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "          \"name\": \"Rouge\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"558bbd7d-1563-45c1-b959-8ad399b0ff9b\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-turquoise.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "          \"name\": \"Bleu\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"))),
            @ApiResponse(responseCode = "404", description = "No products found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "color", required = false) String color) {

        ProductFilter productFilter = new ProductFilter();
        productFilter.setMaxPrice(maxPrice);
        productFilter.setMinPrice(minPrice);
        productFilter.setBrandName(brand);
        productFilter.setColorName(color);

        List<Product> products = productService.getAllProducts(productFilter);
        if (!products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
        }
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get products by store ID", description = "Fetches all products by store ID.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products by store ID", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
                    +
                    "  {\n" +
                    "    \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "    \"name\": \"Escapade\",\n" +
                    "    \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
                    "    \"description\": \"This is an example product description.\",\n" +
                    "    \"price\": 99.99,\n" +
                    "    \"stock\": 50,\n" +
                    "    \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "    \"weight\": 1.5,\n" +
                    "    \"height\": 10.0,\n" +
                    "    \"width\": 20.0,\n" +
                    "    \"createdAt\": \"2024-08-05T16:24:23\",\n" +
                    "    \"category\": {\n" +
                    "      \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "      \"name\": \"Cuisines\",\n" +
                    "      \"uri\": \"cuisines\"\n" +
                    "    },\n" +
                    "    \"subcategory\": {\n" +
                    "      \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "      \"name\": \"Plan de travail\",\n" +
                    "      \"uri\": \"cuisines/plan-de-travail\"\n" +
                    "    },\n" +
                    "    \"brand\": {\n" +
                    "      \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "      \"name\": \"Archideco\",\n" +
                    "      \"uri\": null\n" +
                    "    },\n" +
                    "    \"colors\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Rouge\",\n" +
                    "        \"uri\": null\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "        \"name\": \"Bleu\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"images\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "          \"name\": \"Rouge\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"558bbd7d-1563-45c1-b959-8ad399b0ff9b\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-turquoise.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "          \"name\": \"Bleu\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"))),
            @ApiResponse(responseCode = "404", description = "Store not found or no products found for the store", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Store not found\""))),
            @ApiResponse(responseCode = "404", description = "No products found for store", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for store\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> getProductsByStoreId(@PathVariable UUID storeId) {
        Optional<Store> store = catalogueService.getStoreById(storeId);
        if (store.isPresent() && store.get() != null) {
            if (store.get().getProducts().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for store");
            }
            return ResponseEntity.ok(store.get().getProducts());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found");
        }
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Get products by category UUID", description = "Fetches all products by category UUID.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products by category ID", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
                    +
                    "  {\n" +
                    "    \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "    \"name\": \"Escapade\",\n" +
                    "    \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
                    "    \"description\": \"This is an example product description.\",\n" +
                    "    \"price\": 99.99,\n" +
                    "    \"stock\": 50,\n" +
                    "    \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "    \"weight\": 1.5,\n" +
                    "    \"height\": 10.0,\n" +
                    "    \"width\": 20.0,\n" +
                    "    \"createdAt\": \"2024-08-05T16:24:23\",\n" +
                    "    \"category\": {\n" +
                    "      \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "      \"name\": \"Cuisines\",\n" +
                    "      \"uri\": \"cuisines\"\n" +
                    "    },\n" +
                    "    \"subcategory\": {\n" +
                    "      \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "      \"name\": \"Plan de travail\",\n" +
                    "      \"uri\": \"cuisines/plan-de-travail\"\n" +
                    "    },\n" +
                    "    \"brand\": {\n" +
                    "      \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "      \"name\": \"Archideco\",\n" +
                    "      \"uri\": null\n" +
                    "    },\n" +
                    "    \"colors\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Rouge\",\n" +
                    "        \"uri\": null\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "        \"name\": \"Bleu\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"images\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "          \"name\": \"Rouge\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"558bbd7d-1563-45c1-b959-8ad399b0ff9b\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-turquoise.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "          \"name\": \"Bleu\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"))),
            @ApiResponse(responseCode = "404", description = "No products found for this category", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for this category\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable UUID id) {
        List<Product> products = productService.getProductsByCategoryId(id)
                .orElseThrow(() -> new RuntimeException("No products found for this subcategory"));
        if (products != null && !products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this category");
        }
    }

    @GetMapping("/sub/{id}")
    @Operation(summary = "Get products by subcategory ID", description = "Fetches all products by subcategory UUID.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of products by subcategory ID", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
                    +
                    "  {\n" +
                    "    \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "    \"name\": \"Escapade\",\n" +
                    "    \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
                    "    \"description\": \"This is an example product description.\",\n" +
                    "    \"price\": 99.99,\n" +
                    "    \"stock\": 50,\n" +
                    "    \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "    \"weight\": 1.5,\n" +
                    "    \"height\": 10.0,\n" +
                    "    \"width\": 20.0,\n" +
                    "    \"createdAt\": \"2024-08-05T16:24:23\",\n" +
                    "    \"category\": {\n" +
                    "      \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "      \"name\": \"Cuisines\",\n" +
                    "      \"uri\": \"cuisines\"\n" +
                    "    },\n" +
                    "    \"subcategory\": {\n" +
                    "      \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "      \"name\": \"Plan de travail\",\n" +
                    "      \"uri\": \"cuisines/plan-de-travail\"\n" +
                    "    },\n" +
                    "    \"brand\": {\n" +
                    "      \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "      \"name\": \"Archideco\",\n" +
                    "      \"uri\": null\n" +
                    "    },\n" +
                    "    \"colors\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Rouge\",\n" +
                    "        \"uri\": null\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "        \"name\": \"Bleu\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"images\": [\n" +
                    "      {\n" +
                    "        \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "          \"name\": \"Rouge\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"uuid\": \"558bbd7d-1563-45c1-b959-8ad399b0ff9b\",\n" +
                    "        \"uri\": \"/images/kitchen-ecommerce/escapade-turquoise.png\",\n" +
                    "        \"color\": {\n" +
                    "          \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "          \"name\": \"Bleu\",\n" +
                    "          \"uri\": null\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]"))),
            @ApiResponse(responseCode = "404", description = "No products found for this subcategory", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for this subcategory\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> getProductsBySubcategoryId(@PathVariable UUID id) {
        List<Product> products = productService.getProductsBySubcategoryId(id)
                .orElseThrow(() -> new RuntimeException("No products found for this subcategory"));
        if (products != null && !products.isEmpty()) {
            List<ProductDTO> productsDTO = products.stream().map(Mapper::toProductDTO).collect(Collectors.toList());
            return ResponseEntity.ok(productsDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this subcategory");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by UUID", description = "Fetches a product by its unique identifier.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of product by ID", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    +
                    "  \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "  \"name\": \"Escapade\",\n" +
                    "  \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
                    "  \"description\": \"This is an example product description.\",\n" +
                    "  \"price\": 99.99,\n" +
                    "  \"stock\": 50,\n" +
                    "  \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "  \"weight\": 1.5,\n" +
                    "  \"height\": 10.0,\n" +
                    "  \"width\": 20.0,\n" +
                    "  \"createdAt\": \"2024-08-05T16:24:23\",\n" +
                    "  \"category\": {\n" +
                    "    \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "    \"name\": \"Cuisines\",\n" +
                    "    \"uri\": \"cuisines\"\n" +
                    "  },\n" +
                    "  \"subcategory\": {\n" +
                    "    \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "    \"name\": \"Plan de travail\",\n" +
                    "    \"uri\": \"cuisines/plan-de-travail\"\n" +
                    "  },\n" +
                    "  \"brand\": {\n" +
                    "    \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "    \"name\": \"Archideco\",\n" +
                    "    \"uri\": null\n" +
                    "  },\n" +
                    "  \"colors\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "      \"name\": \"Rouge\",\n" +
                    "      \"uri\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "      \"name\": \"Bleu\",\n" +
                    "      \"uri\": null\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"images\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "      \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
                    "      \"color\": {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Rouge\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"uuid\": \"558bbd7d-1563-45c1-b959-8ad399b0ff9b\",\n" +
                    "      \"uri\": \"/images/kitchen-ecommerce/escapade-turquoise.png\",\n" +
                    "      \"color\": {\n" +
                    "        \"uuid\": \"94ceb468-da88-442d-820e-446cd994144d\",\n" +
                    "        \"name\": \"Bleu\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Product not found\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent() && product.get() != null) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Creates a new product.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    +
                    "  \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "  \"name\": \"New Product\",\n" +
                    "  \"uri\": \"new-product-uri\",\n" +
                    "  \"description\": \"This is a newly created product.\",\n" +
                    "  \"price\": 199.99,\n" +
                    "  \"stock\": 100,\n" +
                    "  \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
                    "  \"weight\": 2.0,\n" +
                    "  \"height\": 15.0,\n" +
                    "  \"width\": 25.0,\n" +
                    "  \"createdAt\": \"2024-08-31T16:24:23\",\n" +
                    "  \"category\": {\n" +
                    "    \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "    \"name\": \"Electronics\",\n" +
                    "    \"uri\": \"electronics\"\n" +
                    "  },\n" +
                    "  \"subcategory\": {\n" +
                    "    \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "    \"name\": \"Smartphones\",\n" +
                    "    \"uri\": \"electronics/smartphones\"\n" +
                    "  },\n" +
                    "  \"brand\": {\n" +
                    "    \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "    \"name\": \"TechBrand\",\n" +
                    "    \"uri\": null\n" +
                    "  },\n" +
                    "  \"colors\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "      \"name\": \"Black\",\n" +
                    "      \"uri\": null\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"images\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "      \"uri\": \"/images/products/new-product.png\",\n" +
                    "      \"color\": {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Black\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid input data\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    +
                    "  \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
                    "  \"name\": \"Updated Product\",\n" +
                    "  \"uri\": \"updated-product-uri\",\n" +
                    "  \"description\": \"This is an updated product description.\",\n" +
                    "  \"price\": 149.99,\n" +
                    "  \"stock\": 80,\n" +
                    "  \"estimatedDelivery\": \"2024-09-01T10:00:00\",\n" +
                    "  \"weight\": 1.8,\n" +
                    "  \"height\": 12.0,\n" +
                    "  \"width\": 22.0,\n" +
                    "  \"createdAt\": \"2024-08-31T16:24:23\",\n" +
                    "  \"category\": {\n" +
                    "    \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
                    "    \"name\": \"Electronics\",\n" +
                    "    \"uri\": \"electronics\"\n" +
                    "  },\n" +
                    "  \"subcategory\": {\n" +
                    "    \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
                    "    \"name\": \"Smartphones\",\n" +
                    "    \"uri\": \"electronics/smartphones\"\n" +
                    "  },\n" +
                    "  \"brand\": {\n" +
                    "    \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
                    "    \"name\": \"TechBrand\",\n" +
                    "    \"uri\": null\n" +
                    "  },\n" +
                    "  \"colors\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "      \"name\": \"Black\",\n" +
                    "      \"uri\": null\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"images\": [\n" +
                    "    {\n" +
                    "      \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
                    "      \"uri\": \"/images/products/updated-product.png\",\n" +
                    "      \"color\": {\n" +
                    "        \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
                    "        \"name\": \"Black\",\n" +
                    "        \"uri\": null\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid input data\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = ""))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Product not found\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}