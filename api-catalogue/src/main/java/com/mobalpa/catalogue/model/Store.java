package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "stores")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
public class Store {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String openingHours;

    @DBRef
    private List<Product> products = new ArrayList<>();
}