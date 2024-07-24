package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Subcategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SubcategoryRepository extends MongoRepository<Subcategory, UUID> {
}
