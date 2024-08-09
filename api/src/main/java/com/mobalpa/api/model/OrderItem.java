package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Map;

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

  @ElementCollection
  @CollectionTable(name = "order_item_properties", joinColumns = @JoinColumn(name = "order_item_uuid"))
  @MapKeyColumn(name = "property_key")
  @Column(name = "property_value")
  private Map<String, String> properties;

  @Column(nullable = false)
  private Integer quantity;
}