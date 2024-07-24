package com.mobalpa.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobalpa.api.dto.UserDTO;
import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.dto.CatalogueProductDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.Role;
import com.mobalpa.api.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToString")
    @Mapping(target = "wishlist", source = "wishlist")
    public abstract UserDTO userToUserDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", expression = "java(hashPassword(userDTO.getPassword()))")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "wishlist", source = "wishlist")
    public abstract User userDTOToUser(UserDTO userDTO);

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "items", source = "items", qualifiedByName = "jsonToProducts")
    public abstract WishlistDTO wishlistToWishlistDTO(Wishlist wishlist);

    @Mapping(target = "user", source = "userUuid", qualifiedByName = "uuidToUser")
    @Mapping(target = "items", source = "items", qualifiedByName = "productsToJson")
    public abstract Wishlist wishlistDTOToWishlist(WishlistDTO wishlistDTO);

    @Named("rolesToString")
    protected String rolesToString(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));
    }

    @Named("uuidToUser")
    protected User uuidToUser(UUID uuid) {
        return userService.getUserByUuid(uuid);
    }

    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("jsonToProducts")
    protected List<CatalogueProductDTO> jsonToProducts(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, CatalogueProductDTO.class));
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to List<ProductDTO>", e);
        }
    }

    @Named("productsToJson")
    protected String productsToJson(List<CatalogueProductDTO> products) {
        try {
            return objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting List<ProductDTO> to JSON", e);
        }
    }
}