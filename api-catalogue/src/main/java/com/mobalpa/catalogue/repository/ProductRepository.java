package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, UUID> {

  @Query("{ 'uuid': { '$in': ?0 } }")
  List<Product> findProductsByIds(List<UUID> ids);

  Optional<Product> findByName(String name);

  List<Product> findBySubcategoryUuid(UUID subcategoryId);
  List<Product> findByCategoryUuid(UUID categoryId);
}
