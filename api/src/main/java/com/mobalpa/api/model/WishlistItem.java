package com.mobalpa.api.model;

import com.mobalpa.api.dto.catalogue.ProductDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "wishlist_item_campaigns",
        joinColumns = @JoinColumn(name = "wishlist_item_uuid"),
        inverseJoinColumns = @JoinColumn(name = "campaign_id")
    )
    private List<Campaign> campaigns = new ArrayList<>();

    @Transient
    private ProductDTO product;

    @Column(nullable = false)
    private String selectedColor;

    @Column(nullable = false)
    private Integer quantity;
}