package com.mobalpa.api.controller;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.service.OrderService;
import com.mobalpa.api.service.PromotionService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        try {
            Order createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getOrder(@PathVariable UUID uuid) {
        try {
            Order order = orderService.getOrderByUuid(uuid);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{uuid}/payment")
    public ResponseEntity<?> completeOrder(@PathVariable UUID uuid) {
        try {
            Order order = orderService.getOrderByUuid(uuid);
            order = orderService.completeOrder(order);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{userUuid}/apply-coupon")
    public ResponseEntity<?> applyCoupon(@PathVariable UUID userUuid, @RequestBody String couponCode) {
        try {
            User user = userService.getUserByUuid(userUuid);
            Optional<CouponCode> coupon = promotionService.getCouponByName(couponCode);
            if (!coupon.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
            }

            Double discountRate = promotionService.claimCoupon(user, coupon.get());
            return ResponseEntity.ok(Map.of("discountRate", discountRate));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{uuid}/track")
    public ResponseEntity<?> trackOrder(@PathVariable UUID uuid) {
        try {
            TrackingDTO trackingDetails = orderService.trackOrder(uuid);
            return ResponseEntity.ok(trackingDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/delivery-prices")
    public ResponseEntity<?> getDeliveryPrices() {
        try {
            List<PriceDTO> deliveryPrices = deliveryService.getDeliveryPrices();
            return ResponseEntity.ok(deliveryPrices);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}