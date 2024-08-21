package com.mobalpa.api.repository;

import com.mobalpa.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByToken(String token);
    User findByEmail(String email);
}
