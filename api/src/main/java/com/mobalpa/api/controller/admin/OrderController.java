package com.mobalpa.api.controller.admin;

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
import java.util.List;
import java.util.Map;

@RestController("AdminOrderController")
@RequestMapping("/api/admin/orders")
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



    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }



}