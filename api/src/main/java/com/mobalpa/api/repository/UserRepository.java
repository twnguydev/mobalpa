package com.mobalpa.api.repository;

import com.mobalpa.api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User findByEmail(String email);
    public Boolean existsByEmail(String email);
}