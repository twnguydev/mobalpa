package com.mobalpa.api.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
public class Payment {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String cardNumber;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @Column(nullable = false)
  private String cvv;

  @Column(nullable = false)
  private String cardHolder;

  @Column(nullable = false)
  private String paypalEmail;

  @Column(nullable = false)
  private String paymentMethod = "CREDIT_CARD";

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();
}