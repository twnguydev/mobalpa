package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;
import java.util.List;

@Document(collection = "stores")
@Data
public class Store {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String openingHours;

    @DBRef
    private List<Product> products;
}