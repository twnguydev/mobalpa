package com.mobalpa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.repository.PaymentRepository;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private OrderService orderService;

  @Autowired
  private UserService userService;

  public Payment processPayment(Order order, Payment payment) {
    userService.getUserByUuid(order.getUser().getUuid());

    orderService.getOrderByUuid(order.getUuid());

    if (order.getTotalTtc() <= 0) {
      throw new IllegalArgumentException("Payment amount must be greater than 0");
    }

    if (!payment.getPaymentMethod().equals("CREDIT_CARD") && !payment.getPaymentMethod().equals("PAYPAL")) {
      throw new IllegalArgumentException("Invalid payment method");
    }

    if (payment.getPaymentMethod().equals("CREDIT_CARD")) {
      if (payment.getCardNumber() == null || payment.getCardNumber().isEmpty()) {
        throw new IllegalArgumentException("Card number is required");
      }
      if (payment.getExpirationDate() == null) {
        throw new IllegalArgumentException("Expiration date is required");
      }
      if (payment.getCvv() == null || payment.getCvv().isEmpty()) {
        throw new IllegalArgumentException("CVV is required");
      }
      if (payment.getCardHolder() == null || payment.getCardHolder().isEmpty()) {
        throw new IllegalArgumentException("Card holder is required");
      }
    } else if (payment.getPaymentMethod().equals("PAYPAL")) {
      if (payment.getPaypalEmail() == null || payment.getPaypalEmail().isEmpty()) {
        throw new IllegalArgumentException("Paypal email is required");
      }
    }

    order.setStatus("PAID");

    return paymentRepository.save(payment);
  }
}
