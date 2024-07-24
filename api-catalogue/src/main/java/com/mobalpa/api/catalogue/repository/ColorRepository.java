package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Color;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ColorRepository extends MongoRepository<Color, UUID> {
}
