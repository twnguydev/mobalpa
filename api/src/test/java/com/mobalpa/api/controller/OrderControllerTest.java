package com.mobalpa.api.controller;

import com.mobalpa.api.dto.CouponRequestDTO;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.dto.delivery.ParcelDTO;
import com.mobalpa.api.dto.delivery.DepotRequest;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PromotionService promotionService;

    @Mock
    private UserService userService;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        ParcelDTO parcel = new ParcelDTO();

        when(orderService.processOrder(any(OrderRequestDTO.class))).thenReturn(parcel);

        ResponseEntity<?> response = orderController.createOrder(orderRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(parcel, response.getBody());
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(new Order());
        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<?> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void testGetOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();

        when(orderService.getOrderByUuid(any(UUID.class))).thenReturn(order);

        ResponseEntity<?> response = orderController.getOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    void testCompleteOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();

        when(orderService.getOrderByUuid(any(UUID.class))).thenReturn(order);

        ResponseEntity<?> response = orderController.completeOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
        verify(orderService, times(1)).completeOrder(order);
    }

    @Test
    void testApplyCouponForUserSuccess() {
        UUID userUuid = UUID.randomUUID();
        CouponRequestDTO couponRequest = new CouponRequestDTO();
        couponRequest.setCouponCode("DISCOUNT10");
        User user = new User();
        CouponCode coupon = new CouponCode();

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(user);
        when(promotionService.getCouponByName(anyString())).thenReturn(coupon);
        Map<String, Object> discountRate = new HashMap<>();
        when(promotionService.claimCoupon(any(User.class), any(CouponCode.class))).thenReturn(discountRate);

        ResponseEntity<?> response = orderController.applyCoupon(userUuid, couponRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discountRate, response.getBody());
    }

    @Test
    void testApplyCouponForUserFailure() {
        UUID userUuid = UUID.randomUUID();
        CouponRequestDTO couponRequest = new CouponRequestDTO();
        couponRequest.setCouponCode("INVALIDCODE");

        when(promotionService.getCouponByName(anyString())).thenReturn(null);

        ResponseEntity<?> response = orderController.applyCoupon(userUuid, couponRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Coupon not found", response.getBody());
    }

    @Test
    void testApplyCouponWithoutUserSuccess() {
        CouponRequestDTO couponRequest = new CouponRequestDTO();
        couponRequest.setCouponCode("DISCOUNT10");
        CouponCode coupon = new CouponCode();

        when(promotionService.getCouponByName(anyString())).thenReturn(coupon);
        Map<String, Object> discountRate = new HashMap<>();
        when(promotionService.claimCoupon(any(), any(CouponCode.class))).thenReturn(discountRate);

        ResponseEntity<?> response = orderController.applyCoupon(couponRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discountRate, response.getBody());
    }

    @Test
    void testApplyCouponWithoutUserFailure() {
        CouponRequestDTO couponRequest = new CouponRequestDTO();
        couponRequest.setCouponCode("INVALIDCODE");

        when(promotionService.getCouponByName(anyString())).thenReturn(null);

        ResponseEntity<?> response = orderController.applyCoupon(couponRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Coupon not found", response.getBody());
    }

    @Test
    void testGetDeliveryPricesSuccess() {
        Map<String, DepotRequest> deliveryPrices = new HashMap<>();
        when(deliveryService.getDeliveryPrices()).thenReturn(deliveryPrices);

        ResponseEntity<?> response = orderController.getDeliveryPrices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deliveryPrices, response.getBody());
    }

    @Test
    void testGetDeliveryPricesFailure() {
        when(deliveryService.getDeliveryPrices()).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = orderController.getDeliveryPrices();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Service error", response.getBody());
    }

    @Test
    void testDownloadInvoiceSuccess() {
        UUID orderId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        byte[] pdfContent = new byte[0];

        when(invoiceService.getInvoiceByOrderUuid(any(UUID.class))).thenReturn(invoice);
        when(invoiceService.generateInvoicePdf(any(Invoice.class))).thenReturn(pdfContent);

        ResponseEntity<byte[]> response = orderController.downloadInvoice(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF_VALUE, response.getHeaders().getContentType().toString());
        assertEquals("attachment; filename=invoice_" + invoice.getInvoiceNumber() + ".pdf", response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
        assertEquals(pdfContent, response.getBody());
    }

    @Test
    void testDownloadInvoiceNotFound() {
        UUID orderId = UUID.randomUUID();

        when(invoiceService.getInvoiceByOrderUuid(any(UUID.class))).thenReturn(null);

        ResponseEntity<byte[]> response = orderController.downloadInvoice(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}