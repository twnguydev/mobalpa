package com.mobalpa.api.repository;

import com.mobalpa.api.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
  Visitor findByEmail(String email);
  Optional<Visitor> findByUuid(UUID uuid);
}