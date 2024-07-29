package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface StoreRepository extends MongoRepository<Store, UUID> {
    List<Product> findProductsByUuid(UUID id);
    Optional<Store> findByName(String name);
}