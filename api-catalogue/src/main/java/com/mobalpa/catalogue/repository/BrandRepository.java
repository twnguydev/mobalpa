package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;

public interface BrandRepository extends MongoRepository<Brand, UUID> {
  Optional<Brand> findByName(String name);
}
