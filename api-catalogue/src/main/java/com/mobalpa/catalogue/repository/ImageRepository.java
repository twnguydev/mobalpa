package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, UUID> {
  Optional<Image> findByUri(String uri);
}
