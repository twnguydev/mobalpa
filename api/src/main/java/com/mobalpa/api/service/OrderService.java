package com.mobalpa.api.service;

import com.mobalpa.api.dto.delivery.*;
import com.mobalpa.api.dto.OrderItemDTO;
import com.mobalpa.api.dto.OrderSummaryDTO;
import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.dto.catalogue.ImageDTO;
import com.mobalpa.api.dto.catalogue.ColorDTO;
import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Visitor;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.Person;
import com.mobalpa.api.model.OrderItem;
import com.mobalpa.api.repository.OrderRepository;
import com.mobalpa.api.repository.PaymentRepository;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.repository.VisitorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class OrderService {

  Logger logger = LoggerFactory.getLogger(OrderService.class);

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private DeliveryService deliveryService;

  @Autowired
  private CatalogueService catalogueService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  private Double VAT = 0.2;

  @Value("${delivery.base-url}")
  private String DELIVERY_SERVICE_URL;

  public List<Order> getAllOrders() {
    List<Order> orders = orderRepository.findAll();
    orders.forEach(order -> order.getItems().size());
    return orders;
  }

  public Order getOrderByUuid(UUID uuid) {
    Order order = orderRepository.findById(uuid)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    order.getItems().forEach(item -> item.getProperties().size());
    return order;
  }

  public Order convertToOrder(OrderSummaryDTO orderSummaryDTO) {
    User user = userRepository.findById(orderSummaryDTO.getUserUuid())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Payment payment = paymentRepository.findById(orderSummaryDTO.getPaymentUuid())
        .orElseThrow(() -> new RuntimeException("Payment not found"));

    Order order = orderRepository.findById(orderSummaryDTO.getUuid())
        .orElseThrow(() -> new RuntimeException("Order not found"));

    Order toOrder = new Order();
    toOrder.setUser(user);
    toOrder.setPayment(payment);
    toOrder.setDeliveryAddress(orderSummaryDTO.getDeliveryAddress());
    toOrder.setDeliveryMethod(order.getDeliveryMethod());
    toOrder.setReduction(order.getReduction());
    toOrder.setTotalHt(order.getTotalHt());
    toOrder.setDeliveryFees(order.getDeliveryFees());
    toOrder.setVat(order.getVat());
    toOrder.setTotalTtc(order.getTotalTtc());
    toOrder.setStatus(order.getStatus());
    toOrder.setDeliveryNumbers(order.getDeliveryNumbers());
    toOrder.setItems(order.getItems());
    return toOrder;
  }

  public OrderSummaryDTO convertToOrderSummaryDTO(Order order) {
    List<OrderItemDTO> itemDTOs = order.getItems().stream()
        .map(this::convertToOrderItemDTO)
        .collect(Collectors.toList());

    OrderSummaryDTO dto = new OrderSummaryDTO();
    dto.setUuid(order.getUuid());
    dto.setUserUuid(order.getUser().getUuid());
    dto.setPaymentUuid(order.getPayment().getUuid());
    dto.setDeliveryAddress(order.getDeliveryAddress());
    dto.setTotalTtc(order.getTotalTtc());
    dto.setStatus(order.getStatus());
    dto.setDeliveryNumbers(order.getDeliveryNumbers());
    dto.setItems(itemDTOs);
    return dto;
  }

  public OrderItem convertToOrderItem(OrderItemDTO itemDTO) {
    OrderItem item = new OrderItem();
    item.setUuid(itemDTO.getUuid());
    item.setProductUuid(itemDTO.getProductUuid());
    item.setQuantity(itemDTO.getQuantity());
    return item;
  }

  public OrderItemDTO convertToOrderItemDTO(OrderItem item) {
    ProductDTO product = catalogueService.getProductById(item.getProductUuid());
    Map<String, String> properties = new HashMap<>();
    properties.put("brand", product.getBrand().getName());
    properties.put("colors", product.getColors().stream()
        .map(ColorDTO::getName)
        .collect(Collectors.joining(", ")));
    properties.put("images", product.getImages().stream()
        .map(ImageDTO::getUri)
        .collect(Collectors.joining(", ")));

    OrderItemDTO dto = new OrderItemDTO();
    dto.setUuid(item.getUuid());
    dto.setProductUuid(item.getProductUuid());
    dto.setProperties(properties);
    dto.setQuantity(item.getQuantity());
    return dto;
  }

  public OrderSummaryDTO getOrderByUuidtoDTO(UUID uuid) {
    OrderSummaryDTO orderSummaryDTO = orderRepository.findById(uuid)
        .map(this::convertToOrderSummaryDTO)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    return orderSummaryDTO;
  }

  @Transactional
  public ParcelDTO processOrder(OrderRequestDTO orderRequestDTO) {
    Person person = userRepository.findById(orderRequestDTO.getUserUuid())
        .<Person>map(u -> u)
        .orElseGet(() -> visitorRepository.findById(orderRequestDTO.getUserUuid())
            .orElseThrow(() -> new RuntimeException("User or Visitor not found")));

    Payment payment = paymentRepository.findById(orderRequestDTO.getPaymentUuid())
        .orElseThrow(() -> new RuntimeException("Payment not found"));

    orderRequestDTO.getItems().forEach(itemDTO -> {
      ProductDTO product = catalogueService.getProductById(itemDTO.getProductUuid());
      if (product == null) {
        throw new RuntimeException("Product with UUID " + itemDTO.getProductUuid() + " not found");
      }
    });

    DepotDTO depot = deliveryService.getDeliveryPrice(orderRequestDTO.getDeliveryMethod())
        .orElseThrow(() -> new RuntimeException("Delivery method not found"));

    Order order = createOrder(orderRequestDTO);

    ParcelDTO delivery = deliveryService.processDelivery(orderRequestDTO, order, depot, person);

    order.setDeliveryNumbers(new ArrayList<>(List.of(delivery.getShipment().getDeliveryNumber())));
    Order createdOrder = orderRepository.save(order);

    if (createdOrder == null) {
      throw new RuntimeException("Order creation failed");
    }

    sendOrderConfirmationEmail(person, createdOrder);

    return delivery;
  }

  public Order createOrder(OrderRequestDTO orderRequestDTO) {
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

    Person person = userRepository.findById(orderRequestDTO.getUserUuid())
        .<Person>map(u -> u)
        .orElseGet(() -> visitorRepository.findById(orderRequestDTO.getUserUuid())
            .orElseThrow(() -> new RuntimeException("User or Visitor not found")));

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

    Optional<DepotDTO> depot = deliveryService.getDeliveryPrice(orderRequestDTO.getDeliveryMethod());
    if (depot.isEmpty()) {
      throw new RuntimeException("Delivery method not found");
    }

    Order order = new Order();
    if (person instanceof User) {
      order.setUser((User) person);
    } else {
      order.setVisitor((Visitor) person);
    }
    order.setPayment(payment.get());
    order.setDeliveryAddress(orderRequestDTO.getDeliveryAddress());
    order.setReduction(orderRequestDTO.getReduction());
    order.setStatus("PENDING");
    order.setTotalHt(orderRequestDTO.getTotalHt());
    order.setDeliveryFees(depot.get().getPrice());
    order.setDeliveryAddress(person.getAddress() + " " + person.getZipcode() + " " + person.getCity());
    order.setDeliveryMethod(orderRequestDTO.getDeliveryMethod());
    order.setVat(orderRequestDTO.getTotalHt() * VAT);
    order.setTotalTtc(order.getTotalHt() + order.getVat() + order.getDeliveryFees() - order.getReduction());

    List<OrderItem> items = orderRequestDTO.getItems().stream()
        .map(itemDTO -> {
          OrderItem item = new OrderItem();
          item.setOrder(order);
          item.setProductUuid(itemDTO.getProductUuid());
          item.setQuantity(itemDTO.getQuantity());
          item.setProperties(itemDTO.getProperties());
          return item;
        }).collect(Collectors.toList());
    order.setItems(items);

    Order createdOrder = orderRepository.save(order);
    if (createdOrder == null) {
      throw new RuntimeException("Order creation failed");
    }

    return createdOrder;
  }

  public Order completeOrder(Order order) {
    if (!"PENDING".equals(order.getStatus())) {
      throw new RuntimeException("Order is not in a PENDING state.");
    }

    order.setStatus("PROCESSED");

    order.getDeliveryNumbers().forEach(deliveryNumber -> {
      try {
        deliveryService.updateDeliveryStatus(deliveryNumber, "SHIPPED");
      } catch (Exception e) {
        throw new RuntimeException("Failed to update delivery status for delivery number: " + deliveryNumber);
      }
    });
    return orderRepository.save(order);
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

  public void sendOrderConfirmationEmail(Person person, Order order) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String formattedIssueDate = order.getCreatedAt().format(formatter);

    try {
      Invoice invoice = invoiceService.createInvoice(order, person);
      byte[] invoicePdf = invoiceService.generateInvoicePdf(invoice);
      invoiceService.saveInvoicePdfToFile(invoice, invoicePdf);

      emailService.sendHtmlEmail(
          person.getEmail(),
          "Confirmation de votre commande - Mobalpa",
          "orderConfirmationTemplate.html",
          invoicePdf,
          "invoice_" + invoice.getInvoiceNumber() + ".pdf",
          "${user.firstName}", person.getFirstname(),
          "${orderNumber}", order.getUuid().toString(),
          "${orderDate}", formattedIssueDate,
          "${deliveryAddress}", order.getDeliveryAddress(),
          "${totalAmount}", String.format("%.2f €", order.getTotalTtc()));
    } catch (MessagingException | IOException e) {
      logger.error("Failed to send email: ", e);
      throw new RuntimeException("There was an issue sending the confirmation email. Please try again later.");
    } catch (Exception e) {
      logger.error("Unexpected error occurred: ", e);
      throw new RuntimeException("An unexpected error occurred while processing your request.");
    }
  }

  // public TrackingDTO trackOrder(UUID uuid) {
  // Order order = orderRepository.findById(uuid).orElseThrow(() -> new
  // RuntimeException("Order not found"));
  // return deliveryService.trackDelivery(order.getDeliveries().get(0).getUuid());
  // }
}