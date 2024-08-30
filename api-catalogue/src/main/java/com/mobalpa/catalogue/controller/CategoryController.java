package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.service.CategoryService;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.dto.CategoryDTO;
import com.mobalpa.catalogue.mapper.Mapper;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/catalogue/categories")
@Tag(name = "Category", description = "APIs for managing categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  @Operation(summary = "Get all categories", description = "Fetches all categories.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of categories successfully retrieved", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "    {\n" +
          "        \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
          "        \"name\": \"Cuisines\",\n" +
          "        \"uri\": \"cuisines\",\n" +
          "        \"description\": \"Ajoutez une touche de modernité à votre intérieur\",\n" +
          "        \"subcategories\": [\n" +
          "            {\n" +
          "                \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "                \"name\": \"Plan de travail\",\n" +
          "                \"uri\": \"cuisines/plan-de-travail\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"uuid\": \"1b10f80b-1373-4091-bc1a-b1ebf7a982b6\",\n" +
          "                \"name\": \"Table\",\n" +
          "                \"uri\": \"cuisines/table\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"uuid\": \"1b10f80b-1373-4095-bc1a-b1ebf7a982b6\",\n" +
          "                \"name\": \"Chaise\",\n" +
          "                \"uri\": \"cuisines/chaise\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"uuid\": \"1b10f80b-1373-4099-bc1a-b1ebf7a982b6\",\n" +
          "                \"name\": \"Décoration\",\n" +
          "                \"uri\": \"cuisines/decoration\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"uuid\": \"1b10f80b-1373-409d-bc1a-b1ebf7a982b6\",\n" +
          "                \"name\": \"Meuble de rangement\",\n" +
          "                \"uri\": \"cuisines/meuble-de-rangement\"\n" +
          "            }\n" +
          "        ],\n" +
          "        \"images\": [\n" +
          "            {\n" +
          "                \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
          "                \"uri\": \"https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp\",\n"
          +
          "                \"color\": null\n" +
          "            }\n" +
          "        ]\n" +
          "    }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "No categories found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No categories found\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories()
        .orElseThrow(() -> new RuntimeException("No categories found"));
    if (categories != null && !categories.isEmpty()) {
      List<CategoryDTO> categoriesDTO = categories.stream()
          .map(Mapper::toCategoryDTO)
          .collect(Collectors.toList());
      return ResponseEntity.ok(categoriesDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories found");
    }
  }

  @GetMapping("/{uuid}")
  @Operation(summary = "Get category by UUID", description = "Fetches a category by its unique identifier.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category successfully retrieved", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "    \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
          "    \"name\": \"Cuisines\",\n" +
          "    \"uri\": \"cuisines\",\n" +
          "    \"description\": \"Ajoutez une touche de modernité à votre intérieur\",\n" +
          "    \"subcategories\": [\n" +
          "        {\n" +
          "            \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "            \"name\": \"Plan de travail\",\n" +
          "            \"uri\": \"cuisines/plan-de-travail\"\n" +
          "        },\n" +
          "        {\n" +
          "            \"uuid\": \"1b10f80b-1373-4091-bc1a-b1ebf7a982b6\",\n" +
          "            \"name\": \"Table\",\n" +
          "            \"uri\": \"cuisines/table\"\n" +
          "        }\n" +
          "    ],\n" +
          "    \"images\": [\n" +
          "        {\n" +
          "            \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
          "            \"uri\": \"https://api.mobalpa.fr/sites/default/files/styles/landscape_2560x1440_2x/public/images/2024/07/01/02-visuelhomepagedesktop5300x2880.webp\",\n"
          +
          "            \"color\": null\n" +
          "        }\n" +
          "    ]\n" +
          "}"))),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Category not found\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getCategoryById(@PathVariable UUID uuid) {
    Optional<Category> category = categoryService.getCategoryById(uuid);
    if (category.isPresent() && category.get() != null) {
      return ResponseEntity.ok(category.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }
  }

  @GetMapping("/{id}/sub")
  @Operation(summary = "Get subcategories by category ID", description = "Fetches all subcategories by category UUID.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Subcategories successfully retrieved", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "    {\n" +
          "        \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "        \"name\": \"Plan de travail\",\n" +
          "        \"uri\": \"cuisines/plan-de-travail\"\n" +
          "    },\n" +
          "    {\n" +
          "        \"uuid\": \"1b10f80b-1373-4091-bc1a-b1ebf7a982b6\",\n" +
          "        \"name\": \"Table\",\n" +
          "        \"uri\": \"cuisines/table\"\n" +
          "    }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "No subcategories found for this category", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No subcategories found for this category\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getSubcategoriesByCategoryId(@PathVariable UUID id) {
    Optional<List<Subcategory>> subcategories = Optional.of(categoryService.getSubcategoriesByCategoryId(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      return ResponseEntity.ok(subcategories.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found for this category");
    }
  }

  @GetMapping("/{id}/products")
  @Operation(summary = "Get products by category ID", description = "Fetches all products by category UUID.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Products successfully retrieved", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "    {\n" +
          "        \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "        \"name\": \"Escapade\",\n" +
          "        \"uri\": \"cuisines/plan-de-travail/escapade\",\n" +
          "        \"description\": \"This is an example product description.\",\n" +
          "        \"price\": 99.99,\n" +
          "        \"stock\": 50,\n" +
          "        \"estimatedDelivery\": \"2024-08-01T10:00:00\",\n" +
          "        \"weight\": 1.5,\n" +
          "        \"height\": 10.0,\n" +
          "        \"width\": 20.0,\n" +
          "        \"createdAt\": \"2024-08-05T16:24:23\",\n" +
          "        \"category\": {\n" +
          "            \"uuid\": \"8493b780-5030-446e-a31c-ca4e6b76af7c\",\n" +
          "            \"name\": \"Cuisines\",\n" +
          "            \"uri\": \"cuisines\"\n" +
          "        },\n" +
          "        \"subcategory\": {\n" +
          "            \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "            \"name\": \"Plan de travail\",\n" +
          "            \"uri\": \"cuisines/plan-de-travail\"\n" +
          "        },\n" +
          "        \"brand\": {\n" +
          "            \"uuid\": \"aa00651c-c697-4714-8c13-4e2c0deadd36\",\n" +
          "            \"name\": \"Archideco\",\n" +
          "            \"uri\": null\n" +
          "        },\n" +
          "        \"colors\": [\n" +
          "            {\n" +
          "                \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
          "                \"name\": \"Rouge\",\n" +
          "                \"uri\": null\n" +
          "            }\n" +
          "        ],\n" +
          "        \"images\": [\n" +
          "            {\n" +
          "                \"uuid\": \"07f59834-1906-4e8c-9c2c-df4219019a69\",\n" +
          "                \"uri\": \"/images/kitchen-ecommerce/escapade-saumon.png\",\n" +
          "                \"color\": {\n" +
          "                    \"uuid\": \"8f864c94-b01e-4f18-a435-ff01e79751f0\",\n" +
          "                    \"name\": \"Rouge\",\n" +
          "                    \"uri\": null\n" +
          "                }\n" +
          "            }\n" +
          "        ]\n" +
          "    }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "No products found for this category", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for this category\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getProductsByCategoryId(@PathVariable UUID id) {
    Optional<List<Subcategory>> subcategories = Optional.of(categoryService.getSubcategoriesByCategoryId(id));
    if (subcategories.isPresent() && !subcategories.get().isEmpty()) {
      List<UUID> subcategoryIds = subcategories.get().stream().map(Subcategory::getUuid).collect(Collectors.toList());
      return ResponseEntity.ok(categoryService.getProductsBySubcategoryIds(subcategoryIds));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this category");
    }
  }

  @PostMapping
  @Operation(summary = "Create category", 
             description = "Creates a new category.", 
             security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", 
                   description = "Category successfully created", 
                   content = @Content(mediaType = "application/json", 
                   examples = @ExampleObject(value = "{\n" +
                      "    \"uuid\": \"64d4ce7c-d754-4934-9e3e-78fb5d613f9c\",\n" +
                      "    \"name\": \"Gros électroménager\",\n" +
                      "    \"description\": \"Catégorie de meubles\",\n" +
                      "    \"uri\": \"gros-electromenager\",\n" +
                      "    \"images\": [\n" +
                      "        {\n" +
                      "            \"uuid\": \"image-uuid\",\n" +
                      "            \"uri\": \"https://example.com/image.jpg\",\n" +
                      "            \"color\": null\n" +
                      "        }\n" +
                      "    ],\n" +
                      "    \"subcategories\": []\n" +
                      "}"
                   ))
      ),
      @ApiResponse(responseCode = "400", 
                   description = "Invalid category data provided", 
                   content = @Content(mediaType = "application/json", 
                   examples = @ExampleObject(value = "\"Invalid category data provided\""))
      ),
      @ApiResponse(responseCode = "401", 
                   description = "Unauthorized", 
                   content = @Content(mediaType = "application/json", 
                   examples = @ExampleObject(value = "\"Unauthorized\""))
      )
  })
  public ResponseEntity<?> createCategory(@RequestBody Category category) {
    try {
      Category createdCategory = categoryService.createCategory(category);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update category", description = "Updates an existing category.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category successfully updated", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "    \"uuid\": \"8de352fd-981b-47f3-9abb-49f72e652275\",\n" +
          "    \"name\": \"Updated Category Name\",\n" +
          "    \"uri\": \"updated-category\",\n" +
          "    \"description\": \"Updated description\",\n" +
          "    \"subcategories\": [\n" +
          "        {\n" +
          "            \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "            \"name\": \"Updated Subcategory\",\n" +
          "            \"uri\": \"updated-subcategory\"\n" +
          "        }\n" +
          "    ],\n" +
          "    \"images\": [\n" +
          "        {\n" +
          "            \"uuid\": \"image-uuid\",\n" +
          "            \"uri\": \"https://example.com/image.jpg\",\n" +
          "            \"color\": null\n" +
          "        }\n" +
          "    ]\n" +
          "}"))),
      @ApiResponse(responseCode = "400", description = "Invalid category data provided", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid category data provided\""))),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Category not found\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
    return ResponseEntity.ok(categoryService.updateCategory(id, category));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete category", description = "Deletes a category.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Category successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Category not found\""))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}