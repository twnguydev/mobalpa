package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface BrandRepository extends MongoRepository<Brand, UUID> {
}
