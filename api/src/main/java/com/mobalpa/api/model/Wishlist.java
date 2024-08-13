package com.mobalpa.api.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlist")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @OneToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items = new ArrayList<>();
}