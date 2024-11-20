package com.mobalpa.catalogue.controller;

import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.service.SubcategoryService;
import com.mobalpa.catalogue.dto.SubcategoryDTO;
import com.mobalpa.catalogue.mapper.Mapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/catalogue/subcategories")
@Tag(name = "Subcategory", description = "APIs for managing subcategories")
public class SubcategoryController {

  @Autowired
  private SubcategoryService subcategoryService;

  @GetMapping
  @Operation(summary = "Get all subcategories", description = "Fetches all subcategories.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of subcategories", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "  {\n" +
          "    \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "    \"name\": \"Plan de travail\",\n" +
          "    \"uri\": \"cuisines/plan-de-travail\",\n" +
          "    \"description\": \"Ajoutez une touche de modernité à votre intérieur\",\n" +
          "    \"products\": [\n" +
          "      {\n" +
          "        \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "        \"name\": \"Escapade\",\n" +
          "        \"uri\": \"cuisines/plan-de-travail/escapade\"\n" +
          "      }\n" +
          "    ]\n" +
          "  }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "No subcategories found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No subcategories found\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getAllSubcategories() {
    List<Subcategory> subcategories = subcategoryService.getAllSubcategories()
        .orElseThrow(() -> new RuntimeException("No subcategories found"));
    if (subcategories != null && !subcategories.isEmpty()) {
      List<SubcategoryDTO> subcategoriesDTO = subcategories.stream().map(Mapper::toSubcategoryDTO)
          .collect(Collectors.toList());
      return ResponseEntity.ok(subcategoriesDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subcategories found");
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get subcategory by ID", description = "Fetches a subcategory by its unique identifier.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of subcategory", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "  \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "  \"name\": \"Plan de travail\",\n" +
          "  \"uri\": \"cuisines/plan-de-travail\",\n" +
          "  \"description\": \"Ajoutez une touche de modernité à votre intérieur\",\n" +
          "  \"products\": [\n" +
          "    {\n" +
          "      \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "      \"name\": \"Escapade\",\n" +
          "      \"uri\": \"cuisines/plan-de-travail/escapade\"\n" +
          "    }\n" +
          "  ]\n" +
          "}"))),
      @ApiResponse(responseCode = "404", description = "Subcategory not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Subcategory not found\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getSubcategoryById(@PathVariable UUID id) {
    Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(id);
    if (subcategory.isPresent() && subcategory.get() != null) {
      return ResponseEntity.ok(subcategory.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subcategory not found");
    }
  }

  @PostMapping
  @Operation(summary = "Create subcategory", description = "Creates a new subcategory.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Subcategory successfully created", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "  \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "  \"name\": \"Plan de travail\",\n" +
          "  \"uri\": \"cuisines/plan-de-travail\",\n" +
          "  \"description\": \"Ajoutez une touche de modernité à votre intérieur\",\n" +
          "  \"products\": [\n" +
          "    {\n" +
          "      \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "      \"name\": \"Escapade\",\n" +
          "      \"uri\": \"cuisines/plan-de-travail/escapade\"\n" +
          "    }\n" +
          "  ]\n" +
          "}"))),
      @ApiResponse(responseCode = "400", description = "Bad request due to invalid input", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid input data\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> createSubcategory(@RequestBody @Valid Subcategory subcategory) {
    try {
      Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update subcategory", description = "Updates a subcategory.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Subcategory successfully updated", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
          +
          "  \"uuid\": \"1b10f80b-1373-408d-bc1a-b1ebf7a982b6\",\n" +
          "  \"name\": \"Plan de travail\",\n" +
          "  \"uri\": \"cuisines/plan-de-travail\",\n" +
          "  \"description\": \"Updated description\",\n" +
          "  \"products\": [\n" +
          "    {\n" +
          "      \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "      \"name\": \"Escapade\",\n" +
          "      \"uri\": \"cuisines/plan-de-travail/escapade\"\n" +
          "    }\n" +
          "  ]\n" +
          "}"))),
      @ApiResponse(responseCode = "400", description = "Bad request due to invalid input", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid input data\""))),
      @ApiResponse(responseCode = "404", description = "Subcategory not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Subcategory not found\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> updateSubcategory(@PathVariable UUID id, @RequestBody Subcategory subcategory) {
    try {
      Subcategory updatedSubcategory = subcategoryService.updateSubcategory(id, subcategory);
      return ResponseEntity.ok(updatedSubcategory);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/{id}/products")
  @Operation(summary = "Get products by subcategory ID", description = "Fetches all products by subcategory UUID.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of products for the subcategory", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\n"
          +
          "  {\n" +
          "    \"uuid\": \"b0dbed68-e512-420c-93e2-72721a1bc23d\",\n" +
          "    \"name\": \"Escapade\",\n" +
          "    \"uri\": \"cuisines/plan-de-travail/escapade\"\n" +
          "  }\n" +
          "]"))),
      @ApiResponse(responseCode = "404", description = "No products found for this subcategory", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"No products found for this subcategory\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<?> getProductsBySubcategoryId(@PathVariable UUID id) {
    Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(id);
    if (subcategory.isPresent() && !subcategoryService.getProductsBySubcategoryId(id).isEmpty()) {
      return ResponseEntity.ok(subcategoryService.getProductsBySubcategoryId(id));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for this subcategory");
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete subcategory", description = "Deletes a subcategory.", security = @SecurityRequirement(name = "apiKey"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Subcategory successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Subcategory not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Subcategory not found\""))),
      @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
  })
  public ResponseEntity<Void> deleteSubcategory(@PathVariable UUID id) {
    subcategoryService.deleteSubcategory(id);
    return ResponseEntity.noContent().build();
  }
}
