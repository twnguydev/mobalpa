package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.UUID;
import java.util.List;

@Document(collection = "category")
@Data
public class Category {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;

    @DBRef
    private List<Subcategory> subcategories;
}
