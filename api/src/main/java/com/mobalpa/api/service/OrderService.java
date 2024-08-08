package com.mobalpa.api.service;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.dto.catalogue.CatalogueImageDTO;
import com.mobalpa.api.dto.catalogue.ColorDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.OrderItem;
import com.mobalpa.api.repository.OrderRepository;
import com.mobalpa.api.repository.PaymentRepository;
import com.mobalpa.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DeliveryService deliveryService;

  @Autowired
  private CatalogueService catalogueService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

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

  @Transactional
  public ParcelDTO createOrder(OrderRequestDTO orderRequestDTO) {
    if (orderRequestDTO.getUserUuid() == null) {
      throw new RuntimeException("User UUID is required");
    }
    if (orderRequestDTO.getPaymentUuid() == null) {
      throw new RuntimeException("Payment UUID is required");
    }
    if (orderRequestDTO.getItems() == null || orderRequestDTO.getItems().isEmpty()) {
      throw new RuntimeException("Items are required");
    }
    if (orderRequestDTO.getTotalHt() == null) {
      throw new RuntimeException("Total HT is required");
    }
    if (orderRequestDTO.getDeliveryMethod() == null) {
      throw new RuntimeException("Delivery method is required");
    }
    if (orderRequestDTO.getReduction() == null) {
      orderRequestDTO.setReduction(0.0);
    }

    Optional<User> user = userRepository.findById(orderRequestDTO.getUserUuid());
    if (user.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    Optional<Payment> payment = paymentRepository.findById(orderRequestDTO.getPaymentUuid());
    if (payment.isEmpty()) {
      throw new RuntimeException("Payment not found");
    }

    orderRequestDTO.getItems().forEach(itemDTO -> {
      ProductDTO product = catalogueService.getProductById(itemDTO.getProductUuid());
      if (product == null) {
        throw new RuntimeException("Product with UUID " + itemDTO.getProductUuid() + " not found");
      }
    });

    Optional<DepotDTO> price = deliveryService.getDeliveryPrice(orderRequestDTO.getDeliveryMethod());
    if (price.isEmpty()) {
      throw new RuntimeException("Delivery method not found");
    }

    Order order = new Order();
    order.setUser(user.get());
    order.setPayment(payment.get());
    order.setDeliveryAddress(orderRequestDTO.getDeliveryAddress());
    order.setReduction(orderRequestDTO.getReduction());
    order.setStatus("PENDING");
    order.setTotalHt(orderRequestDTO.getTotalHt());
    order.setDeliveryFees(price.get().getPrice());
    order.setDeliveryAddress(user.get().getAddress() + " " + user.get().getZipcode() + " " + user.get().getCity());
    order.setDeliveryMethod(orderRequestDTO.getDeliveryMethod());
    order.setVat(orderRequestDTO.getTotalHt() * VAT);
    order.setTotalTtc(order.getTotalHt() + order.getVat() + order.getDeliveryFees() - order.getReduction());

    List<OrderItem> items = orderRequestDTO.getItems().stream()
        .map(itemDTO -> {
          OrderItem item = new OrderItem();
          item.setOrder(order);
          item.setProductUuid(itemDTO.getProductUuid());
          item.setQuantity(itemDTO.getQuantity());
          return item;
        }).collect(Collectors.toList());
    order.setItems(items);

    DeliveryRequestDTO deliveryRequest = new DeliveryRequestDTO();
    deliveryRequest.setOrderUuid(order.getUuid());
    deliveryRequest.setParcelItems(orderRequestDTO.getItems().stream().map(itemDTO -> {
      ParcelItemDTO parcelItemDTO = new ParcelItemDTO();
      ProductDTO product = catalogueService.getProductById(itemDTO.getProductUuid());
      parcelItemDTO.setDescription(product.getDescription());
      parcelItemDTO.setProductUuid(product.getUuid());
      Map<String, String> properties = new HashMap<>();
      properties.put("brand", product.getBrand().getName());
      properties.put("colors", product.getColors().stream().map(ColorDTO::getName).collect(Collectors.joining(", ")));
      properties.put("images",
          product.getImages().stream().map(CatalogueImageDTO::getUri).collect(Collectors.joining(", ")));
      parcelItemDTO.setProperties(properties);
      parcelItemDTO.setQuantity(itemDTO.getQuantity());
      parcelItemDTO.setValue(product.getPrice());
      parcelItemDTO.setWeight(product.getWeight());
      parcelItemDTO.setWidth(product.getWidth());
      parcelItemDTO.setHeight(product.getHeight());
      return parcelItemDTO;
    }).collect(Collectors.toList()));
    deliveryRequest.setShippingMethodCheckoutName(orderRequestDTO.getDeliveryMethod());
    deliveryRequest.setSenderAddress(price.get().getAddress());
    deliveryRequest.setRecipientAddress(user.get().getAddress());
    deliveryRequest.setRecipientPhoneNumber(user.get().getPhoneNumber());
    deliveryRequest.setRecipientEmail(user.get().getEmail());
    deliveryRequest.setRecipientName(user.get().getFirstname() + " " + user.get().getLastname());

    ParcelDTO delivery = deliveryService.createDelivery(deliveryRequest);
    if (delivery == null) {
      throw new RuntimeException("Delivery creation failed");
    }

    order.setDeliveryNumbers(List.of(delivery.getShipment().getDeliveryNumber()));
    Order createdOrder = orderRepository.save(order);
    if (createdOrder == null) {
      throw new RuntimeException("Order creation failed");
    }

    return delivery;
  }

  // public Order completeOrder(Order order) {
  // order.setStatus("COMPLETED");
  // Order completedOrder = orderRepository.save(order);

  // deliveryService.updateDeliveryStatus(order.getDeliveries().get(0).getUuid(),
  // "SHIPPED");
  // return completedOrder;
  // }

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

  // public TrackingDTO trackOrder(UUID uuid) {
  // Order order = orderRepository.findById(uuid).orElseThrow(() -> new
  // RuntimeException("Order not found"));
  // return deliveryService.trackDelivery(order.getDeliveries().get(0).getUuid());
  // }
}