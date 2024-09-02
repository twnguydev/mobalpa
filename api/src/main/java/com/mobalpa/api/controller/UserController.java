package com.mobalpa.api.controller;

import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.mobalpa.api.dto.PaymentRequestDTO;
import com.mobalpa.api.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users, customers", description = "APIs for managing users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private WishlistService wishlistService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/{uuid}")
  @Operation(summary = "Get user by UUID", description = "Fetches a user by their unique identifier.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, parameters = @Parameter(name = "uuid", description = "UUID of the user to fetch", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), responses = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
              "firstname": "Tanguy",
              "lastname": "Gibrat",
              "email": "tanguy.gibrat@epitech.eu",
              "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
              "phoneNumber": "068100000000",
              "birthdate": "2001-06-24",
              "address": "1 Place Jean Jaurès",
              "zipcode": "13001",
              "city": "Marseille",
              "token": null,
              "active": true,
              "createdAt": "2024-08-26T16:58:00",
              "updatedAt": "2024-08-27T09:56:59",
              "roles": [
                  {
                      "id": 1,
                      "name": "ROLE_USER"
                  }
              ],
              "tickets": [
                  {
                      "uuid": "e5f53dee-ee0e-4355-9487-046fd2737786",
                      "type": "ORDER",
                      "name": "Demande d'échange",
                      "issue": "Problème sur cafetière",
                      "createdAt": "2024-08-29T10:18:35"
                  }
              ],
              "respondedTickets": [],
              "username": "tanguy.gibrat@epitech.eu",
              "authorities": [
                  {
                      "authority": "ROLE_USER"
                  }
              ],
              "accountNonExpired": true,
              "accountNonLocked": true,
              "credentialsNonExpired": true,
              "enabled": true
          }
          """))),
      @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found."
          """)))
  })
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    try {
      User user = userService.getUserByUuid(uuid);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping
  @Operation(summary = "Create user", description = "Creates a new user.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
      {
          "firstname": "Tanguy",
          "lastname": "Gibrat",
          "email": "tanguy.gibrat@epitech.eu",
          "password": "password123",
          "phoneNumber": "0600000000",
          "birthdate": "2001-06-24"
      }
      """))), responses = {
      @ApiResponse(responseCode = "201", description = "User created successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
              "firstname": "Tanguy",
              "lastname": "Gibrat",
              "email": "tanguy.gibrat@epitech.eu",
              "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
              "phoneNumber": "0600000000",
              "birthdate": "2001-06-24",
              "address": null,
              "zipcode": null,
              "city": null,
              "token": null,
              "active": true,
              "createdAt": "2024-08-26T16:58:00",
              "updatedAt": "2024-08-27T09:56:59",
              "roles": [
                  {
                      "id": 1,
                      "name": "ROLE_USER"
                  }
              ],
              "tickets": [],
              "respondedTickets": [],
              "username": "tanguy.gibrat@epitech.eu",
              "authorities": [],
              "accountNonExpired": true,
              "accountNonLocked": true,
              "credentialsNonExpired": true,
              "enabled": true
          }
          """))),
      @ApiResponse(responseCode = "400", description = "Invalid input data.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "Invalid data provided."
          """)))
  })
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      User createdUser = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{uuid}")
  @Operation(summary = "Update user", description = "Updates an existing user.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, parameters = @Parameter(name = "uuid", description = "UUID of the user to update", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
      {
          "address": "456 Updated St",
          "zipcode": "98765",
          "city": "Paris",
          "roles": [
              {
                  "name": "ROLE_STORE_MANAGER"
              },
              {
                  "name": "ROLE_ADMIN"
              }
          ]
      }
      """))), responses = {
      @ApiResponse(responseCode = "200", description = "User updated successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          {
              "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
              "firstname": "Tanguy",
              "lastname": "Gibrat",
              "email": "tanguy.gibrat@epitech.eu",
              "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
              "phoneNumber": "0600000000",
              "birthdate": "2001-06-24",
              "address": "456 Updated St",
              "zipcode": "98765",
              "city": "Paris",
              "token": null,
              "active": true,
              "createdAt": "2024-08-26T16:58:00",
              "updatedAt": "2024-08-30T10:00:00",
              "roles": [
                  {
                      "id": 3,
                      "name": "ROLE_STORE_MANAGER"
                  },
                  {
                      "id": 2,
                      "name": "ROLE_ADMIN"
                  }
              ],
              "tickets": [
                  {
                      "uuid": "e5f53dee-ee0e-4355-9487-046fd2737786",
                      "type": "ORDER",
                      "name": "Demande d'échange",
                      "issue": "Problème sur cafetière",
                      "createdAt": "2024-08-29T10:18:35"
                  }
              ],
              "respondedTickets": [],
              "username": "tanguy.gibrat@epitech.eu",
              "authorities": [
                  {
                      "authority": "ROLE_ADMIN"
                  },
                  {
                      "authority": "ROLE_STORE_MANAGER"
                  }
              ],
              "accountNonExpired": true,
              "accountNonLocked": true,
              "credentialsNonExpired": true,
              "enabled": true
          }
          """))),
      @ApiResponse(responseCode = "400", description = "Invalid input data.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "Invalid data provided."
          """))),
      @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found."
          """)))
  })
  public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
    try {
      System.out.println("User: " + user);
      User updatedUser = userService.updateUser(uuid, user);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Delete user", description = "Deletes a user.", security = {
      @SecurityRequirement(name = "bearerAuth"),
      @SecurityRequirement(name = "apiKeyAuth")
  }, parameters = @Parameter(name = "uuid", description = "UUID of the user to delete", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), responses = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User deleted"
          """))),
      @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found."
          """)))
  })
  public ResponseEntity<?> deleteUser(@PathVariable UUID uuid) {
    userService.deleteUser(uuid);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
  }

  @GetMapping("/{id}/wishlist")
  @Operation(summary = "Get user's wishlist", description = "Fetches a user's wishlist.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "id", description = "UUID of the user whose wishlist is to be fetched", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), responses = {
      @ApiResponse(responseCode = "200", description = "Wishlist fetched successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wishlist.class), examples = @ExampleObject(value = """
          {
              "uuid": "5728d306-9c1d-4fba-af67-59dab25ec526",
              "user": {
                  "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                  "firstname": "Tanguy",
                  "lastname": "Gibrat",
                  "email": "tanguy.gibrat@epitech.eu",
                  "address": "1 Place Jean Jaurès",
                  "zipcode": "13001",
                  "city": "Marseille",
                  "createdAt": "2024-08-26T16:58:00",
                  "updatedAt": "2024-08-27T09:56:59",
                  "roles": [
                      {"id": 2, "name": "ROLE_ADMIN"},
                      {"id": 3, "name": "ROLE_STORE_MANAGER"}
                  ],
                  "username": "tanguy.gibrat@epitech.eu"
              },
              "items": [
                  {
                      "uuid": "abbf29e6-c98e-4315-b5eb-720ed375f510",
                      "productUuid": "94bb7d31-4e60-6e69-c9d9-45366daea469",
                      "campaigns": [
                          {
                              "id": 2,
                              "type": "SUBCATEGORY",
                              "name": "Déstockage sur les Plans de travail",
                              "discountRate": 20.0,
                              "dateStart": "2024-08-22T11:54:59",
                              "dateEnd": "2024-08-27T23:59:59"
                          }
                      ],
                      "product": {
                          "uuid": "94bb7d31-4e60-6e69-c9d9-45366daea469",
                          "name": "Rétro",
                          "description": "This is an example product description.",
                          "price": 70.0,
                          "stock": 12,
                          "estimatedDelivery": "2024-08-01T10:00:00",
                          "weight": 3.0,
                          "height": 12.5,
                          "width": 30.0,
                          "uri": "cuisines/plan-de-travail/retro",
                          "category": {
                              "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                              "name": "Cuisines",
                              "uri": "cuisines"
                          },
                          "subcategory": {
                              "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                              "name": "Plan de travail",
                              "uri": "cuisines/plan-de-travail"
                          },
                          "brand": {
                              "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                              "name": "Archideco"
                          },
                          "colors": [
                              {"uuid": "812f6d92-1f7d-67b9-1df6-a546a98ea5e4", "name": "Jaune"},
                              {"uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0", "name": "Rouge"}
                          ]
                      },
                      "selectedColor": "Rouge",
                      "properties": {
                          "brand": "Archideco",
                          "images": "/images/kitchen-ecommerce/retro-jaune.png"
                      },
                      "quantity": 1
                  }
              ]
          }
          """))),
      @ApiResponse(responseCode = "404", description = "User or wishlist not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found"
          """)))
  })
  public ResponseEntity<?> getWishlist(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    Wishlist wishlist = user.getWishlist();
    if (wishlist == null || wishlist.getItems().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no items in his wishlist");
    }

    wishlist.getItems().forEach(item -> {
      ProductDTO product = catalogueService.getProductById(item.getProductUuid());
      if (product != null) {
        item.setProduct(product);
      }
    });

    return ResponseEntity.ok(wishlist);
  }

  @PatchMapping("/{id}/wishlist")
  @Operation(summary = "Modify user's wishlist", description = "Modifies a user's wishlist.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "id", description = "UUID of the user whose wishlist is to be modified", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body containing the action and item details to be modified in the wishlist", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishlistDTO.class), examples = @ExampleObject(value = """
      {
          "action": "add",
          "item": {
              "productUuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
              "selectedColor": "Bleu",
              "quantity": 8
          }
      }
      """))), responses = {
      @ApiResponse(responseCode = "200", description = "Wishlist modified successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wishlist.class), examples = @ExampleObject(value = """
          {
              "uuid": "5728d306-9c1d-4fba-af67-59dab25ec526",
              "user": {
                  "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                  "firstname": "Tanguy",
                  "lastname": "Gibrat",
                  "email": "tanguy.gibrat@epitech.eu",
                  "address": "1 Place Jean Jaurès",
                  "zipcode": "13001",
                  "city": "Marseille",
                  "createdAt": "2024-08-26T16:58:00",
                  "updatedAt": "2024-08-27T09:56:59",
                  "roles": [
                      {"id": 2, "name": "ROLE_ADMIN"},
                      {"id": 3, "name": "ROLE_STORE_MANAGER"}
                  ],
                  "username": "tanguy.gibrat@epitech.eu"
              },
              "items": [
                  {
                      "uuid": "abbf29e6-c98e-4315-b5eb-720ed375f510",
                      "productUuid": "94bb7d31-4e60-6e69-c9d9-45366daea469",
                      "product": {
                          "uuid": "94bb7d31-4e60-6e69-c9d9-45366daea469",
                          "name": "Rétro",
                          "description": "This is an example product description.",
                          "price": 70.0,
                          "stock": 12,
                          "estimatedDelivery": "2024-08-01T10:00:00",
                          "weight": 3.0,
                          "height": 12.5,
                          "width": 30.0,
                          "uri": "cuisines/plan-de-travail/retro",
                          "category": {
                              "uuid": "8493b780-5030-446e-a31c-ca4e6b76af7c",
                              "name": "Cuisines",
                              "uri": "cuisines"
                          },
                          "subcategory": {
                              "uuid": "1b10f80b-1373-408d-bc1a-b1ebf7a982b6",
                              "name": "Plan de travail",
                              "uri": "cuisines/plan-de-travail"
                          },
                          "brand": {
                              "uuid": "aa00651c-c697-4714-8c13-4e2c0deadd36",
                              "name": "Archideco"
                          },
                          "colors": [
                              {"uuid": "812f6d92-1f7d-67b9-1df6-a546a98ea5e4", "name": "Jaune"},
                              {"uuid": "8f864c94-b01e-4f18-a435-ff01e79751f0", "name": "Rouge"}
                          ]
                      },
                      "selectedColor": "Rouge",
                      "properties": {
                          "brand": "Archideco",
                          "images": "/images/kitchen-ecommerce/retro-jaune.png"
                      },
                      "quantity": 1
                  },
                  {
                      "uuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                      "productUuid": "b0dbed68-e512-420c-93e2-72721a1bc23d",
                      "selectedColor": "Bleu",
                      "quantity": 8
                  }
              ]
          }
          """))),
      @ApiResponse(responseCode = "400", description = "Invalid action or requested color not available for the product", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "Invalid action for wishlist"
          """)))
  })
  public ResponseEntity<?> modifyWishlist(@PathVariable UUID id, @RequestBody WishlistDTO request) {
    Wishlist wishlist;
    switch (request.getAction().toLowerCase()) {
      case "add":
        wishlist = wishlistService.addToWishlist(id, request.getItem());
        break;
      case "remove":
        wishlist = wishlistService.removeFromWishlist(id, request.getItem().getProductUuid(),
            request.getItem().getQuantity());
        break;
      default:
        return ResponseEntity.badRequest().body("Invalid action for wishlist");
    }
    return ResponseEntity.ok(wishlist);
  }

  @GetMapping("/{id}/orders")
  @Operation(summary = "Get user's orders", description = "Fetches a user's orders.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "id", description = "UUID of the user whose orders are to be fetched", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), responses = {
      @ApiResponse(responseCode = "200", description = "Orders fetched successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class), examples = @ExampleObject(value = """
          [
              {
                  "uuid": "09b77a1f-3024-4009-90c6-515f9c883a12",
                  "user": {
                      "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                      "firstname": "Tanguy",
                      "lastname": "Gibrat",
                      "email": "tanguy.gibrat@epitech.eu",
                      "phoneNumber": "0600000000",
                      "birthdate": "2001-06-24",
                      "address": "1 Place Jean Jaurès",
                      "zipcode": "13001",
                      "city": "Marseille",
                      "createdAt": "2024-08-26T16:58:00",
                      "updatedAt": "2024-08-27T09:56:59",
                      "roles": [
                          {"id": 2, "name": "ROLE_ADMIN"},
                          {"id": 3, "name": "ROLE_STORE_MANAGER"}
                      ],
                      "username": "tanguy.gibrat@epitech.eu"
                  },
                  "items": [
                      {
                          "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                          "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                          "properties": {},
                          "quantity": 1
                      }
                  ],
                  "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
                  "reduction": 49.998,
                  "deliveryFees": 0.0,
                  "deliveryMethod": "Mobalpa Centrale",
                  "vat": 23.9984,
                  "totalHt": 119.992,
                  "totalTtc": 93.9924,
                  "status": "PROCESSED",
                  "deliveryNumbers": ["MOB_JLwKgYmeszeD"],
                  "payment": {
                      "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
                      "cardHolder": "Tanguy Gibrat",
                      "paypalEmail": "tanguy.gibrat@epitech.eu",
                      "paymentMethod": "PAYPAL",
                      "createdAt": "2024-08-26T17:00:32"
                  },
                  "createdAt": "2024-08-27T14:14:08"
              }
          ]
          """))),
      @ApiResponse(responseCode = "404", description = "User not found or no orders available", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found"
          """)))
  })
  public ResponseEntity<?> getOrders(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    if (user.getOrders().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no orders");
    }

    return ResponseEntity.ok(user.getOrders());
  }

  @GetMapping("/{id}/payments")
  @Operation(summary = "Get user's payments", description = "Fetches a user's payments.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "id", description = "UUID of the user whose payments are to be fetched", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), responses = {
      @ApiResponse(responseCode = "200", description = "Payments fetched successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class), examples = @ExampleObject(value = """
          [
              {
                  "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
                  "user": {
                      "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                      "firstname": "Tanguy",
                      "lastname": "Gibrat",
                      "email": "tanguy.gibrat@epitech.eu",
                      "phoneNumber": "0600000000",
                      "birthdate": "2001-06-24",
                      "address": "1 Place Jean Jaurès",
                      "zipcode": "13001",
                      "city": "Marseille",
                      "createdAt": "2024-08-26T16:58:00",
                      "updatedAt": "2024-08-27T09:56:59",
                      "roles": [
                          {"id": 2, "name": "ROLE_ADMIN"},
                          {"id": 3, "name": "ROLE_STORE_MANAGER"}
                      ],
                      "username": "tanguy.gibrat@epitech.eu"
                  },
                  "cardNumber": "",
                  "expirationDate": null,
                  "cvv": "",
                  "cardHolder": "Tanguy Gibrat",
                  "paypalEmail": "tanguy.gibrat@epitech.eu",
                  "paymentMethod": "PAYPAL",
                  "createdAt": "2024-08-26T17:00:32"
              }
          ]
          """))),
      @ApiResponse(responseCode = "404", description = "User not found or no payments available", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found"
          """)))
  })
  public ResponseEntity<?> getPayments(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    if (user.getPayments().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no payments");
    }

    return ResponseEntity.ok(user.getPayments());
  }

  @PostMapping("/{id}/payments")
  @Operation(summary = "Add payment to user", description = "Adds a payment to a user.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "id", description = "UUID of the user to whom the payment will be added", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment details to be added", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentRequestDTO.class), examples = @ExampleObject(value = """
      {
          "cardNumber": "1234567812345678",
          "expirationDate": "2025-12-31",
          "cvv": "123",
          "cardHolder": "Tanguy Gibrat",
          "paypalEmail": "",
          "paymentMethod": "CREDIT_CARD"
      }
      """))), responses = {
      @ApiResponse(responseCode = "201", description = "Payment added successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class), examples = @ExampleObject(value = """
          {
              "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
              "user": {
                  "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                  "firstname": "Tanguy",
                  "lastname": "Gibrat",
                  "email": "tanguy.gibrat@epitech.eu",
                  "phoneNumber": "0600000000",
                  "birthdate": "2001-06-24",
                  "address": "1 Place Jean Jaurès",
                  "zipcode": "13001",
                  "city": "Marseille",
                  "createdAt": "2024-08-26T16:58:00",
                  "updatedAt": "2024-08-27T09:56:59",
                  "roles": [
                      {"id": 2, "name": "ROLE_ADMIN"},
                      {"id": 3, "name": "ROLE_STORE_MANAGER"}
                  ],
                  "username": "tanguy.gibrat@epitech.eu"
              },
              "cardNumber": "1234567812345678",
              "expirationDate": "2025-12-31",
              "cvv": "123",
              "cardHolder": "Tanguy Gibrat",
              "paypalEmail": null,
              "paymentMethod": "CREDIT_CARD",
              "createdAt": "2024-08-26T17:00:32"
          }
          """))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found"
          """)))
  })
  public ResponseEntity<?> addPaymentToUser(@PathVariable UUID id, @RequestBody PaymentRequestDTO paymentDTO) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = userOptional.get();

    Payment payment = new Payment();
    payment.setUser(user);
    payment.setCardNumber(paymentDTO.getCardNumber());
    payment.setExpirationDate(paymentDTO.getExpirationDate());
    payment.setCvv(paymentDTO.getCvv());
    payment.setCardHolder(paymentDTO.getCardHolder());
    payment.setPaypalEmail(paymentDTO.getPaypalEmail());
    payment.setPaymentMethod(paymentDTO.getPaymentMethod());

    Payment savedPayment = paymentRepository.save(payment);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
  }

  @DeleteMapping("/{id}/payments/{paymentId}")
  @Operation(summary = "Delete payment", description = "Deletes a payment associated with a specific user.", security = @SecurityRequirement(name = "bearerAuth"), parameters = {
      @Parameter(name = "id", description = "UUID of the user whose payment is to be deleted", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"),
      @Parameter(name = "paymentId", description = "UUID of the payment to be deleted", required = true, example = "b68b03a0-d860-47d1-a60c-659d20653dd3")
  }, responses = {
      @ApiResponse(responseCode = "204", description = "Payment deleted successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "Payment deleted"
          """))),
      @ApiResponse(responseCode = "404", description = "User or payment not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
          "User not found"
          """)))
  })
  public ResponseEntity<?> deletePayment(@PathVariable UUID id, @PathVariable UUID paymentId) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = userOptional.get();
    Optional<Payment> paymentOptional = user.getPayments().stream().filter(p -> p.getUuid().equals(paymentId))
        .findFirst();
    if (paymentOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
    }

    Payment payment = paymentOptional.get();
    paymentRepository.delete(payment);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payment deleted");
  }
}
