package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "`order`")
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid = UUID.randomUUID();

  @ManyToOne
  private User user;

  @Column(nullable = true)
  private Date warranty;

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

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<OrderItem> items = new ArrayList<>();

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
    if ("COMPLETED".equals(status) && this.warranty == null) {
      this.warranty = Date.from(LocalDateTime.now().plusYears(7).atZone(ZoneId.systemDefault()).toInstant());
    }
  }
}