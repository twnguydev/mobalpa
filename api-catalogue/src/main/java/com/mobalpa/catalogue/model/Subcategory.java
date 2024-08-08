package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "subcategory")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
public class Subcategory {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
    private String description;
    private String uri;

    @DBRef
    private List<Product> products = new ArrayList<>();

    @DBRef
    private Category category;
}
