package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "images")
@Data
public class Image {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String uri;

    @DBRef
    private Color color;
}