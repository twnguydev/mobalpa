package com.mobalpa.catalogue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.service.CatalogueService;
import com.mobalpa.catalogue.model.Store;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/catalogue")
@Tag(name = "Catalogue", description = "APIs for managing the catalogue")
public class CatalogueController {

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/store")
  @Operation(summary = "Get all stores", description = "Fetches all stores.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of all stores", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "  {\n" +
          "    \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "    \"name\": \"Archidéco Marseille\",\n" +
          "    \"address\": \"123 Avenue de la Valentine 13011 Marseille\",\n" +
          "    \"phoneNumber\": \"0100000000\",\n" +
          "    \"email\": \"archideco@marseille.fr\",\n" +
          "    \"openingHours\": \"Lun-Sam 09h-18h\",\n" +
          "    \"products\": []\n" +
          "  }\n" +
          "]"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getAllStores() {
    return ResponseEntity.ok(catalogueService.getAllStores());
  }

  @GetMapping("/store/{storeId}")
  @Operation(summary = "Get store data by store ID", description = "Fetches store data by store ID.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of products by store ID", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "  {\n" +
          "    \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "    \"name\": \"Archidéco Marseille\",\n" +
          "    \"address\": \"123 Avenue de la Valentine 13011 Marseille\",\n" +
          "    \"phoneNumber\": \"0100000000\",\n" +
          "    \"email\": \"archideco@marseille.com\",\n" +
          "    \"openingHours\": \"Lun-Sam 09h-18h\",\n" +
          "    \"products\": [\n" +
          "      {\n" +
          "        \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "        \"name\": \"Table basse\",\n" +
          "        \"description\": \"Table basse en bois\",\n" +
          "        \"price\": 100.0,\n" +
          "        \"color\": \"Bois\",\n" +
          "        \"brand\": \"Ikea\",\n" +
          "        \"subcategory\": {\n" +
          "          \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "          \"name\": \"Tables basses\"\n" +
          "        },\n" +
          "        \"category\": {\n" +
          "          \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "          \"name\": \"Meubles\"\n" +
          "        },\n" +
          "        \"images\": [\n" +
          "          {\n" +
          "            \"uuid\": \"e093b860-1bc5-4ae6-8fb7-4348c0ab9960\",\n" +
          "            \"uri\": \"https://www.ikea.com/tables-basses/1234.jpg\"\n" +
          "          }\n" +
          "        ]\n" +
          "      }\n" +
          "    ]\n" +
          "  }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "Store not found or no products found for the store", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Store not found\""))),
      @ApiResponse(responseCode = "404", description = "No products found for store", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for store\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
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

  @PostMapping("/store")
  @Operation(summary = "Create store", description = "Creates a new store.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Store successfully created", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "  \"uuid\": \"8de352fd-981b-47f3-9abb-49f72e652275\",\n" +
          "  \"name\": \"Archidéco Aubagne\",\n" +
          "  \"address\": \"123 Avenue de la Valentine 13011 Marseille\",\n" +
          "  \"phoneNumber\": \"0100000000\",\n" +
          "  \"email\": \"archideco@marseille.fr\",\n" +
          "  \"openingHours\": null,\n" +
          "  \"products\": []\n" +
          "}"))),
      @ApiResponse(responseCode = "400", description = "Bad request due to invalid input or store already exists", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Store with name Archidéco Aubagne already exists\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> createStore(@RequestBody Store store) {
    try {
      Store createdStore = catalogueService.createStore(store);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/best-sellers")
  @Operation(summary = "Get best sellers", description = "Fetches the best selling products.", security = @SecurityRequirement(name = "apiKey"))
  public ResponseEntity<?> getBestSellers() {
    try {
      List<Product> bestSellers = catalogueService.getBestSellers();
      if (bestSellers.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No best sellers found");
      } else {
        return ResponseEntity.ok(bestSellers);
      }
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}