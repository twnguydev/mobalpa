package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "product")
@Data
public class Product {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime estimatedDelivery;
    private double weight;
    private double height;
    private double width;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @DBRef
    private Category category;

    @DBRef
    private Subcategory subcategory;
    
    @DBRef
    private Brand brand;
    
    @DBRef
    private List<Color> colors;

    @DBRef
    private List<Image> images;

    @DBRef
    private List<Store> stores;
}