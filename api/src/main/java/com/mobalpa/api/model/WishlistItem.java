package com.mobalpa.api.model;

import com.mobalpa.api.dto.catalogue.ProductDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wishlist_item")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_uuid", nullable = false)
    @JsonIgnore
    private Wishlist wishlist;

    @Column(nullable = false)
    private UUID productUuid;

    @Transient
    private ProductDTO product;

    @Column(nullable = false)
    private String selectedColor;

    @ElementCollection
    @CollectionTable(name = "wishlist_item_properties", joinColumns = @JoinColumn(name = "wishlist_item_uuid"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    private Map<String, String> properties;

    @Column(nullable = false)
    private Integer quantity;
}