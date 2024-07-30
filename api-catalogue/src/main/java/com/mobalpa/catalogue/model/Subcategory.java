package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.UUID;
import java.util.List;

@Document(collection = "subcategory")
@Data
public class Subcategory {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;

    @DBRef
    @JsonBackReference
    private Category category;

    @DBRef
    private List<Product> products;
}
