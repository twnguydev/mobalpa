package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Data
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_uuid", nullable = false)
  private Order order;

  @Column(nullable = false)
  private UUID productUuid;

  @Column(nullable = false)
  private Integer quantity;
}