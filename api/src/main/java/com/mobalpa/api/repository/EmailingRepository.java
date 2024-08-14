package com.mobalpa.api.repository;

import com.mobalpa.api.model.Emailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailingRepository extends JpaRepository<Emailing, UUID> {
    Emailing findByUuid(UUID uuid);
}

