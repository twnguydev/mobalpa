package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "`order`")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid = UUID.randomUUID();

  @ManyToOne
  @JoinColumn(name = "user_uuid", nullable = true)
  @JsonIgnoreProperties("orders")
  private User user;

  @ManyToOne
  @JoinColumn(name = "visitor_uuid", nullable = true)
  @JsonIgnoreProperties("orders")
  private Visitor visitor;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @Column(nullable = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate warranty;

  @Column(nullable = false)
  private String deliveryAddress;

  @Column(nullable = false)
  private Double reduction;

  @Column(nullable = false)
  private Double deliveryFees;

  @Column(nullable = false)
  private String deliveryMethod;

  @Column(nullable = false)
  private Double vat;

  @Column(nullable = false)
  private Double totalHt;

  @Column(nullable = false)
  private Double totalTtc;

  @Column(nullable = false)
  private String status = "PENDING";

  @ElementCollection
  @CollectionTable(name = "order_deliveries", joinColumns = @JoinColumn(name = "order_uuid"))
  @Column(name = "delivery_number")
  private List<String> deliveryNumbers = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  private Payment payment;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt = LocalDateTime.now();

  public void setStatus(String status) {
    this.status = status;
    if ("PROCESSED".equals(status) && this.warranty == null) {
      this.warranty = LocalDate.now().plusYears(7);
    }
  }
}