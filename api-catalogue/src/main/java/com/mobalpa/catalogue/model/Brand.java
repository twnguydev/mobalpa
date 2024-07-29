package com.mobalpa.catalogue.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "brand")
@Data
public class Brand {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String name;
}
