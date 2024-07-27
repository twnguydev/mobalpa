package com.mobalpa.api.service;

import com.mobalpa.api.model.Order;
import com.mobalpa.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  public Order getOrderByUuid(UUID uuid) {
    return orderRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Order not found"));
  }

  public Order createOrder(Order order) {
    order.setStatus("PENDING");
    return orderRepository.save(order);
  }

  public Order completeOrder(Order order) {
    order.setStatus("COMPLETED");
    return orderRepository.save(order);
  }
}