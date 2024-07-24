package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CategoryRepository extends MongoRepository<Category, UUID> {
}
