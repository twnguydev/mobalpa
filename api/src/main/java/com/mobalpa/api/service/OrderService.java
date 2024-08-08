package com.mobalpa.api.service;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.OrderItem;
import com.mobalpa.api.repository.OrderRepository;
import com.mobalpa.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DeliveryService deliveryService;

  @Autowired
  private UserRepository userRepository;

  private Double VAT = 0.2;

  @Value("${delivery.base-url}")
  private String DELIVERY_SERVICE_URL;

  public List<Order> getAllOrders() {
    List<Order> orders = orderRepository.findAll();
    if (orders.isEmpty()) {
      throw new RuntimeException("No orders found");
    }
    return orders;
  }

  public Order getOrderByUuid(UUID uuid) {
    return orderRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Order not found"));
  }

  public Order createOrder(OrderRequestDTO orderRequestDTO) {
    Optional<User> user = userRepository.findById(orderRequestDTO.getUserUuid());
    if (user.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    PriceDTO price = deliveryService.getDeliveryPrice(orderRequestDTO.getDeliveryMethod());

    Order order = new Order();
    order.setUser(user.get());
    order.setDeliveryAddress(orderRequestDTO.getDeliveryAddress());
    order.setReduction(orderRequestDTO.getReduction());
    order.setStatus("PENDING");
    order.setTotalHt(orderRequestDTO.getTotalHt());
    order.setDeliveryFees(price.getPrice());
    order.setVat(orderRequestDTO.getTotalHt() * VAT);
    order.setTotalTtc(order.getTotalHt() + order.getVat() + order.getDeliveryFees() - order.getReduction());

    List<OrderItem> items = orderRequestDTO.getItems().stream()
        .map(itemDTO -> new OrderItem(itemDTO.getProductId(), itemDTO.getQuantity()))
        .collect(Collectors.toList());
    order.setItems(items);

    DeliveryRequestDTO deliveryRequest = new DeliveryRequestDTO();
    deliveryRequest.setOrderUuid(order.getUuid());
    deliveryRequest.setAddress(order.getDeliveryAddress());
    deliveryRequest.setDeliveryMethod(orderRequestDTO.getDeliveryMethod());
    DeliveryDTO delivery = deliveryService.createDelivery(deliveryRequest);

    order.setDeliveries(List.of(delivery));
    return orderRepository.save(order);
  }

  public Order completeOrder(Order order) {
    order.setStatus("COMPLETED");
    Order completedOrder = orderRepository.save(order);

    deliveryService.updateDeliveryStatus(order.getDeliveries().get(0).getUuid(), "SHIPPED");
    return completedOrder;
  }

  public Order updateOrder(UUID uuid, OrderRequestDTO orderDetails) {
    Order order = orderRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Order not found"));

    if (orderDetails.getDeliveryAddress() != null) {
      order.setDeliveryAddress(orderDetails.getDeliveryAddress());
    }
    if (orderDetails.getDeliveryMethod() != null) {
      order.setDeliveryMethod(orderDetails.getDeliveryMethod());
    }
    return orderRepository.save(order);
  }

  public TrackingDTO trackOrder(UUID uuid) {
    Order order = orderRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Order not found"));
    return deliveryService.trackDelivery(order.getDeliveries().get(0).getUuid());
  }
}