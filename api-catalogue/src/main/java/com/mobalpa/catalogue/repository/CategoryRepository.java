package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, UUID> {
  Optional<Category> findByName(String name);
}
