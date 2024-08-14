package com.mobalpa.api.controller;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.CouponRequestDTO;
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
        ParcelDTO createdOrder = orderService.processOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getOrder(@PathVariable UUID uuid) {
        return ResponseEntity.ok(orderService.getOrderByUuid(uuid));
    }

    @PostMapping("/{uuid}/payment")
    public ResponseEntity<?> completeOrder(@PathVariable UUID uuid) {
        Order order = orderService.getOrderByUuid(uuid);
        orderService.completeOrder(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{userUuid}/apply-coupon")
    public ResponseEntity<?> applyCoupon(@PathVariable UUID userUuid, @RequestBody CouponRequestDTO couponRequest) {
        User user = userService.getUserByUuid(userUuid);
        CouponCode coupon = promotionService.getCouponByName(couponRequest.getCouponCode().trim());
        Double discountRate = promotionService.claimCoupon(user, coupon);
        return ResponseEntity.ok(Map.of("discountRate", discountRate));
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
    public ResponseEntity<?> getDeliveryPrices() {
        try {
            List<DepotDTO> deliveryPrices = deliveryService.getDeliveryPrices();
            return ResponseEntity.ok(deliveryPrices);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}