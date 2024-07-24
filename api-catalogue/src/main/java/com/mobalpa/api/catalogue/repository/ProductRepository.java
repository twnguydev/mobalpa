package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {
}
