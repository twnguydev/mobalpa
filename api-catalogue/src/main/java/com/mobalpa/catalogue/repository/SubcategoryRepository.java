package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Subcategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;

public interface SubcategoryRepository extends MongoRepository<Subcategory, UUID> {
  Optional<Subcategory> findByName(String name);
}
