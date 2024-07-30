package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "product")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
public class Product {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private LocalDateTime estimatedDelivery;
    private Double weight;
    private Double height;
    private Double width;
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