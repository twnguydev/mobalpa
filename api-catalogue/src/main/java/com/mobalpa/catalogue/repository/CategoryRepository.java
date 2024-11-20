package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, UUID> {
  Optional<Category> findByName(String name);
  Optional<Category> findByUri(String uri);
  List<Category> findBySubcategoriesUuidIn(List<UUID> subcategoryUuids);
}
