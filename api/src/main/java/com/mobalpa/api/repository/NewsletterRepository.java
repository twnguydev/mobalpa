package com.mobalpa.api.repository;

import com.mobalpa.api.model.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NewsletterRepository extends JpaRepository<Newsletter, UUID> {
    @Query("SELECT n FROM Newsletter n JOIN FETCH n.user")
    List<Newsletter> findAllWithUsers();
    boolean existsByUser_Uuid(UUID userUuid);
}