package com.mobalpa.api.repository;

import com.mobalpa.api.model.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface SatisfactionRepository extends JpaRepository<Satisfaction, UUID> {
    Optional<Satisfaction> findByUuid(UUID uuid);
    List<Satisfaction> findByTargetUuidAndTargetType(UUID targetUuid, String targetType);
    List<Satisfaction> findByTargetType(String targetType);
}
