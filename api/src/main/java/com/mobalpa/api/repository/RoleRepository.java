package com.mobalpa.api.repository;

import com.mobalpa.api.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  boolean existsByName(String name);
  Optional<Role> findByName(String name);
}