package com.mobalpa.api.service;

import com.mobalpa.api.model.Role;
import com.mobalpa.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @PostConstruct
  public void initRoles() {
    List<String> roleNames = Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_STORE_MANAGER");

    for (String roleName : roleNames) {
      if (!roleRepository.existsByName(roleName)) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
      }
    }
  }

  public Role getRoleByName(String name) {
    Optional<Role> role = Optional.ofNullable(roleRepository.findByName(name));
    return role.orElse(null);
  }
}