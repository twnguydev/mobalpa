package com.mobalpa.catalogue.repository;

import com.mobalpa.catalogue.model.Color;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ColorRepository extends MongoRepository<Color, UUID> {
}
