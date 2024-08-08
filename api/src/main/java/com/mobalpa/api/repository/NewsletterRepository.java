package com.mobalpa.api.repository;

import com.mobalpa.api.model.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, UUID> {
    @Transactional
    void deleteByEmailUser(String emailUser);
}