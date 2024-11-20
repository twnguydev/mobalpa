package com.mobalpa.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @Column(nullable = false)
  private String invoiceNumber;

  @Column(nullable = false)
  private LocalDateTime issueDate;

  @Column(nullable = false)
  private Double totalAmount;

  @Column(nullable = false)
  private Double vatAmount;

  @Column(nullable = false)
  private Double amountExcludingTax;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_uuid", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_uuid", nullable = true)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "visitor_uuid", nullable = true)
  private Visitor visitor;
}