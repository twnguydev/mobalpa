package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "`order`")
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @Column(columnDefinition = "json", nullable = false)
  private String items;

  @ManyToOne
  private User user;

  @Column(nullable = true)
  private Date warranty;

  @Column(nullable = false)
  private String deliveryAddress;

  @Column(nullable = false)
  private Long reduction;

  @Column(nullable = false)
  private Long deliveryFees;

  @Column(nullable = false)
  private Long vat;

  @Column(nullable = false)
  private Long totalHt;

  @Column(nullable = false)
  private Long totalTtc;

  @Column(nullable = false)
  private String status = "PENDING";

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  public void setStatus(String status) {
    this.status = status;
    if ("COMPLETED".equals(status) && this.warranty == null) {
      this.warranty = Date.from(LocalDateTime.now().plusYears(7).atZone(ZoneId.systemDefault()).toInstant());
    }
  }
}