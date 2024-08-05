package com.mobalpa.api.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private String productId;
  private int quantity;

  public OrderItem() {
  }

  public OrderItem(String productId, int quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }
}