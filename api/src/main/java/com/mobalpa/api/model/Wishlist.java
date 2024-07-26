package com.mobalpa.api.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    private User user;

    @Column(columnDefinition = "json", nullable = false)
    private String items;
}

