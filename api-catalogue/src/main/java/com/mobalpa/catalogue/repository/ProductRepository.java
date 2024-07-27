package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {

  @Query("{ 'uuid': { '$in': ?0 } }")
  List<Product> findProductsByIds(List<UUID> ids);
}
