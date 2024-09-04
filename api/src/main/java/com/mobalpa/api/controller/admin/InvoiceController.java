package com.mobalpa.api.controller.admin;

import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.InvoiceService;
import com.mobalpa.api.service.OrderService;
import com.mobalpa.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/invoices")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Invoice management for admin users", description = "APIs for managing invoices for admin users")
public class InvoiceController {

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private UserService userService;

  @PostMapping("/create")
  @Operation(summary = "Create invoice", description = "Creates a new invoice based on the provided order and user UUIDs.", security = @SecurityRequirement(name = "bearerAuth"), parameters = {
      @Parameter(name = "orderUuid", description = "UUID of the order for which the invoice is being created", required = true, example = "b67b77d3-2a61-4a68-9f64-8d55b9b25cd3"),
      @Parameter(name = "userUuid", description = "UUID of the user to whom the invoice will be assigned", required = true, example = "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9")
  }, responses = {
      @ApiResponse(responseCode = "201", description = "Invoice created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class), examples = @ExampleObject(value = """
          {
              "uuid": "cf2dbb8d-8b1d-4d54-bd77-43bca5b798d7",
              "invoiceNumber": "INV-12345",
              "issueDate": "2024-08-27T14:14:08",
              "totalAmount": 119.99,
              "vatAmount": 23.99,
              "amountExcludingTax": 96.00,
              "order": {
                  "uuid": "b67b77d3-2a61-4a68-9f64-8d55b9b25cd3",
                  "user": {
                      "uuid": "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9",
                      "firstname": "Tanguy",
                      "lastname": "Gibrat",
                      "email": "tanguy.gibrat@epitech.eu",
                      "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
                      "phoneNumber": "0600000000",
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
                              "id": 2,
                              "name": "ROLE_ADMIN"
                          },
                          {
                              "id": 3,
                              "name": "ROLE_STORE_MANAGER"
                          }
                      ],
                      "tickets": [],
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
                  },
                  "visitor": null,
                  "items": [
                      {
                          "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                          "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                          "properties": {},
                          "quantity": 1
                      }
                  ],
                  "warranty": null,
                  "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
                  "reduction": 49.998,
                  "deliveryFees": 0.0,
                  "deliveryMethod": "Mobalpa Centrale",
                  "vat": 23.9984,
                  "totalHt": 119.992,
                  "totalTtc": 93.9924,
                  "status": "PROCESSED",
                  "deliveryNumbers": [
                      "MOB_JLwKgYmeszeD"
                  ],
                  "payment": {
                      "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
                      "user": {},
                      "visitor": null,
                      "cardNumber": "",
                      "expirationDate": null,
                      "cvv": "",
                      "cardHolder": "Tanguy Gibrat",
                      "paypalEmail": "tanguy.gibrat@epitech.eu",
                      "paymentMethod": "PAYPAL",
                      "createdAt": "2024-08-26T17:00:32"
                  },
                  "createdAt": "2024-08-27T14:14:08"
              },
              "user": {
                  "uuid": "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9",
                  "firstname": "Tanguy",
                  "lastname": "Gibrat",
                  "email": "tanguy.gibrat@epitech.eu",
                  "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
                  "phoneNumber": "0600000000",
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
                          "id": 2,
                          "name": "ROLE_ADMIN"
                      },
                      {
                          "id": 3,
                          "name": "ROLE_STORE_MANAGER"
                      }
                  ],
                  "tickets": [
                      {
                          "uuid": "e5f53dee-ee0e-4355-9487-046fd2737786",
                          "type": "ORDER",
                          "name": "Demande d'échange",
                          "issue": "Problème sur cafetière",
                          "createdAt": "2024-08-29T10:18:35",
                          "updatedAt": null,
                          "closedAt": null,
                          "responder": null,
                          "resolution": null
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
          }
          """)))
  })
  public ResponseEntity<?> createInvoice(@RequestParam UUID orderUuid, @RequestParam UUID userUuid) {
    Order order = orderService.getOrderByUuid(orderUuid);
    User user = userService.getUserByUuid(userUuid);
    Invoice invoice = invoiceService.createInvoice(order, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
  }

  @GetMapping("/{invoiceNumber}")
  @Operation(summary = "Get invoice by Invoice Number", description = "Fetches an invoice by its unique invoice number.", security = @SecurityRequirement(name = "bearerAuth"), parameters = {
      @Parameter(name = "invoiceNumber", description = "Unique identifier of the invoice", required = true, example = "INV-12345")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Invoice fetched successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class), examples = @ExampleObject(value = """
          {
              "uuid": "cf2dbb8d-8b1d-4d54-bd77-43bca5b798d7",
              "invoiceNumber": "INV-12345",
              "issueDate": "2024-08-27T14:14:08",
              "totalAmount": 119.99,
              "vatAmount": 23.99,
              "amountExcludingTax": 96.00,
              "order": {
                  "uuid": "b67b77d3-2a61-4a68-9f64-8d55b9b25cd3",
                  "user": {},
                  "visitor": null,
                  "items": [
                      {
                          "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                          "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                          "properties": {},
                          "quantity": 1
                      }
                  ],
                  "warranty": null,
                  "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
                  "reduction": 49.998,
                  "deliveryFees": 0.0,
                  "deliveryMethod": "Mobalpa Centrale",
                  "vat": 23.9984,
                  "totalHt": 119.992,
                  "totalTtc": 93.9924,
                  "status": "PROCESSED",
                  "deliveryNumbers": [
                      "MOB_JLwKgYmeszeD"
                  ],
                  "payment": {
                      "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
                      "user": {},
                      "visitor": null,
                      "cardNumber": "",
                      "expirationDate": null,
                      "cvv": "",
                      "cardHolder": "Tanguy Gibrat",
                      "paypalEmail": "tanguy.gibrat@epitech.eu",
                      "paymentMethod": "PAYPAL",
                      "createdAt": "2024-08-26T17:00:32"
                  },
                  "createdAt": "2024-08-27T14:14:08"
              },
              "user": {
                  "uuid": "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9",
                  "firstname": "Tanguy",
                  "lastname": "Gibrat",
                  "email": "tanguy.gibrat@epitech.eu",
                  "password": "$2a$10$UDWliuRKI.nLGf3HkqfHyONUr/et1znU7zNXJVvd.62rp2UTIguo2",
                  "phoneNumber": "0600000000",
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
                          "id": 2,
                          "name": "ROLE_ADMIN"
                      },
                      {
                          "id": 3,
                          "name": "ROLE_STORE_MANAGER"
                      }
                  ],
                  "tickets": [
                      {
                          "uuid": "e5f53dee-ee0e-4355-9487-046fd2737786",
                          "type": "ORDER",
                          "name": "Demande d'échange",
                          "issue": "Problème sur cafetière",
                          "createdAt": "2024-08-29T10:18:35",
                          "updatedAt": null,
                          "closedAt": null,
                          "responder": null,
                          "resolution": null
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
          }
          """))),
      @ApiResponse(responseCode = "404", description = "Invoice not found.")
  })
  public ResponseEntity<?> getInvoice(@PathVariable String invoiceNumber) {
    return ResponseEntity.ok(invoiceService.getInvoiceByInvoiceNumber(invoiceNumber));
  }

  @GetMapping
  @Operation(
      summary = "Get all invoices",
      description = "Fetches all invoices.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Invoices fetched successfully.",
              content = @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = Invoice.class)
                  ),
                  examples = @ExampleObject(value = """
                  [
                      {
                          "uuid": "cf2dbb8d-8b1d-4d54-bd77-43bca5b798d7",
                          "invoiceNumber": "INV-12345",
                          "issueDate": "2024-08-27T14:14:08",
                          "totalAmount": 119.99,
                          "vatAmount": 23.99,
                          "amountExcludingTax": 96.00,
                          "order": {
                              "uuid": "b67b77d3-2a61-4a68-9f64-8d55b9b25cd3",
                              "user": {
                                  "uuid": "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9",
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
                                      {
                                          "id": 2,
                                          "name": "ROLE_ADMIN"
                                      },
                                      {
                                          "id": 3,
                                          "name": "ROLE_STORE_MANAGER"
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
                              },
                              "items": [
                                  {
                                      "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                                      "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
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
                              "deliveryNumbers": [
                                  "MOB_JLwKgYmeszeD"
                              ],
                              "payment": {
                                  "uuid": "b68b03a0-d860-47d1-a60c-659d20653dd3",
                                  "user": {
                                      "uuid": "d4e7c93e-45f4-4d9e-8783-9ab21e86b2d9",
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
                                          {
                                              "id": 2,
                                              "name": "ROLE_ADMIN"
                                          },
                                          {
                                              "id": 3,
                                              "name": "ROLE_STORE_MANAGER"
                                          }
                                      ],
                                      "tickets": [
                                          {
                                              "uuid": "e5f53dee-ee0e-4355-9487-046fd2737786",
                                              "type": "ORDER",
                                              "name": "Demande d'échange",
                                              "issue": "Problème sur cafetière",
                                              "createdAt": "2024-08-29T10:18:35"
                                          },
                                          {
                                              "uuid": "1a77d876-7659-4551-92b4-6cfcead9bfd2",
                                              "type": "ORDER",
                                              "name": "sdfghjklsdfghjk",
                                              "issue": "qsdfghjklmsdfghjklmù",
                                              "createdAt": "2024-08-27T13:50:59"
                                          }
                                      ],
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
                                  },
                                  "cardNumber": "",
                                  "expirationDate": null,
                                  "cvv": "",
                                  "cardHolder": "Tanguy Gibrat",
                                  "paypalEmail": "tanguy.gibrat@epitech.eu",
                                  "paymentMethod": "PAYPAL",
                                  "createdAt": "2024-08-26T17:00:32"
                              },
                              "createdAt": "2024-08-27T14:14:08"
                          }
                      }
                  ]
                  """)
              )
          )
      }
  )
  public ResponseEntity<?> getAllInvoices() {
      return ResponseEntity.ok(invoiceService.getAllInvoices());
  }  
}