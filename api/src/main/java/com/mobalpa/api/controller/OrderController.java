package com.mobalpa.api.controller;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.CouponRequestDTO;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.service.OrderService;
import com.mobalpa.api.service.PromotionService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.InvoiceService;
import com.mobalpa.api.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "APIs for managing orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
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
                "vat": 23.998,
                "totalHt": 119.992,
                "totalTtc": 93.992,
                "status": "PROCESSED"
            }
            """))), responses = {
            @ApiResponse(responseCode = "201", description = "Order created successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "uuid": "09b77a1f-3024-4009-90c6-515f9c883a12",
                        "user": {
                            "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                            "firstname": "Tanguy",
                            "lastname": "Gibrat",
                            "email": "tanguy.gibrat@epitech.eu",
                            "address": "1 Place Jean Jaurès",
                            "zipcode": "13001",
                            "city": "Marseille"
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
                        "vat": 23.998,
                        "totalHt": 119.992,
                        "totalTtc": 93.992,
                        "status": "PROCESSED"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid order details provided.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Invalid order details provided."
                    """)))
    })
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        ParcelDTO createdOrder = orderService.processOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Fetches all orders.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    [
                        {
                            "uuid": "09b77a1f-3024-4009-90c6-515f9c883a12",
                            "user": {
                                "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                                "firstname": "Tanguy",
                                "lastname": "Gibrat",
                                "email": "tanguy.gibrat@epitech.eu"
                            },
                            "items": [
                                {
                                    "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                                    "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                                    "quantity": 1
                                }
                            ],
                            "deliveryAddress": "1 Place Jean Jaurès 13005 Marseille",
                            "reduction": 49.998,
                            "deliveryFees": 0.0,
                            "deliveryMethod": "Mobalpa Centrale",
                            "vat": 23.998,
                            "totalHt": 119.992,
                            "totalTtc": 93.992,
                            "status": "PROCESSED"
                        }
                    ]
                    """)))
    })
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get order by UUID", description = "Fetches an order by its unique identifier.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, parameters = @Parameter(name = "uuid", description = "UUID of the order to fetch", required = true, example = "09b77a1f-3024-4009-90c6-515f9c883a12"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "uuid": "09b77a1f-3024-4009-90c6-515f9c883a12",
                        "user": {
                            "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                            "firstname": "Tanguy",
                            "lastname": "Gibrat",
                            "email": "tanguy.gibrat@epitech.eu"
                        },
                        "items": [
                            {
                                "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                                "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                                "quantity": 1
                            }
                        ],
                        "deliveryAddress": "1 Place Jean Jaurès 13005 Marseille",
                        "reduction": 49.998,
                        "deliveryFees": 0.0,
                        "deliveryMethod": "Mobalpa Centrale",
                        "vat": 23.998,
                        "totalHt": 119.992,
                        "totalTtc": 93.992,
                        "status": "PROCESSED"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Order not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Order not found."
                    """)))
    })
    public ResponseEntity<?> getOrder(@PathVariable UUID uuid) {
        return ResponseEntity.ok(orderService.getOrderByUuid(uuid));
    }

    @PostMapping("/{uuid}/payment")
    @Operation(summary = "Process payment", description = "Processes payment for an order.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, parameters = @Parameter(name = "uuid", description = "UUID of the order to process payment for", required = true, example = "09b77a1f-3024-4009-90c6-515f9c883a12"), responses = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "uuid": "09b77a1f-3024-4009-90c6-515f9c883a12",
                        "user": {
                            "uuid": "07266275-9098-4ec3-b315-930eb18c9616",
                            "firstname": "Tanguy",
                            "lastname": "Gibrat",
                            "email": "tanguy.gibrat@epitech.eu"
                        },
                        "items": [
                            {
                                "uuid": "38a9214b-f725-498e-9c26-79de90729e0f",
                                "productUuid": "bd5d5362-2d36-6f1c-5efd-f6255fdb288f",
                                "quantity": 1
                            }
                        ],
                        "deliveryAddress": "1 Place Jean Jaurès 13005 Marseille",
                        "reduction": 49.998,
                        "deliveryFees": 0.0,
                        "deliveryMethod": "Mobalpa Centrale",
                        "vat": 23.998,
                        "totalHt": 119.992,
                        "totalTtc": 93.992,
                        "status": "PROCESSED"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Order not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Order not found."
                    """)))
    })
    public ResponseEntity<?> completeOrder(@PathVariable UUID uuid) {
        Order order = orderService.getOrderByUuid(uuid);
        orderService.completeOrder(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{userUuid}/apply-coupon")
    @Operation(summary = "Apply coupon to user's account", description = "Applies a coupon to a user's account.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, parameters = @Parameter(name = "userUuid", description = "UUID of the user to apply the coupon to", required = true, example = "07266275-9098-4ec3-b315-930eb18c9616"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
                "couponCode": "SUMMER2024"
            }
            """))), responses = {
            @ApiResponse(responseCode = "200", description = "Coupon applied successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "discount": 20.0
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Coupon or user not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Coupon or user not found."
                    """)))
    })
    public ResponseEntity<?> applyCoupon(@PathVariable UUID userUuid, @RequestBody CouponRequestDTO couponRequest) {
        User user = userService.getUserByUuid(userUuid);
        CouponCode coupon = promotionService.getCouponByName(couponRequest.getCouponCode().trim());
        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> discountRate = promotionService.claimCoupon(user, coupon);
        return ResponseEntity.ok(discountRate);
    }

    @PostMapping("/apply-coupon")
    @Operation(summary = "Apply coupon to an order", description = "Applies a coupon to an order.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
                "couponCode": "SUMMER2024"
            }
            """))), responses = {
            @ApiResponse(responseCode = "200", description = "Coupon applied successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "discount": 20.0
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Coupon not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Coupon not found."
                    """)))
    })
    public ResponseEntity<?> applyCoupon(@RequestBody CouponRequestDTO couponRequest) {
        CouponCode coupon = promotionService.getCouponByName(couponRequest.getCouponCode().trim());
        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }

        Map<String, Object> discountRate = promotionService.claimCoupon(null, coupon);
        return ResponseEntity.ok(discountRate);
    }

    // @GetMapping("/{uuid}/track")
    // public ResponseEntity<?> trackOrder(@PathVariable UUID uuid) {
    // try {
    // // TrackingDTO trackingDetails = orderService.trackOrder(uuid);
    // return ResponseEntity.ok(trackingDetails);
    // } catch (RuntimeException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    // }
    // }

    @GetMapping("/delivery-prices")
    @Operation(summary = "Get delivery prices", description = "Fetches delivery prices.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved delivery prices.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "standard": {
                            "price": 0.0,
                            "description": "Standard delivery"
                        },
                        "express": {
                            "price": 10.0,
                            "description": "Express delivery"
                        }
                    }
                    """)))
    })
    public ResponseEntity<?> getDeliveryPrices() {
        try {
            Map<String, DepotRequest> deliveryPrices = deliveryService.getDeliveryPrices();
            return ResponseEntity.ok(deliveryPrices);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{orderUuid}/invoice")
    @Operation(summary = "Download invoice", description = "Downloads an invoice for an order.", security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "apiKeyAuth")
    }, parameters = @Parameter(name = "orderUuid", description = "UUID of the order to download invoice for", required = true, example = "09b77a1f-3024-4009-90c6-515f9c883a12"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved invoice.", content = @Content(mediaType = "application/pdf", examples = @ExampleObject(value = """
                    [Binary PDF Data]
                    """))),
            @ApiResponse(responseCode = "404", description = "Invoice not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Invoice not found."
                    """)))
    })
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable UUID orderUuid) {
        Invoice invoice = invoiceService.getInvoiceByOrderUuid(orderUuid);
        if (invoice != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=invoice_" + invoice.getInvoiceNumber() + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            return new ResponseEntity<>(invoiceService.generateInvoicePdf(invoice), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}