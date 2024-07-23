package com.mobalpa.api.mapper;

import com.mobalpa.api.dto.UserDTO;
import com.mobalpa.api.model.Role;
import com.mobalpa.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToString")
  public abstract UserDTO userToUserDTO(User user);

  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "password", expression = "java(hashPassword(userDTO.getPassword()))")
  @Mapping(target = "authorities", ignore = true)
  public abstract User userDTOToUser(UserDTO userDTO);

  @Named("rolesToString")
  protected String rolesToString(Set<Role> roles) {
    return roles.stream()
        .map(Role::getName)
        .collect(Collectors.joining(", "));
  }

  protected String hashPassword(String password) {
    return passwordEncoder.encode(password);
  }
}
