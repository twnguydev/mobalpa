package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Subcategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SubcategoryRepository extends MongoRepository<Subcategory, UUID> {
}
