package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "category")
@Data
public class Category {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;

    @DBRef
    @JsonManagedReference
    private List<Subcategory> subcategories = new ArrayList<>();
}
