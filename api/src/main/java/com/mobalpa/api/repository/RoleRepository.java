package com.mobalpa.api.repository;

import com.mobalpa.api.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}