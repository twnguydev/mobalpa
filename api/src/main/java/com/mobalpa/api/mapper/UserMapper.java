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
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserService userService;

    @Mapping(target = "wishlist", source = "wishlist", qualifiedByName = "wishlistToWishlistDTO")
    @Mapping(target = "role", source = "roles", qualifiedByName = "rolesToString")
    public abstract UserDTO userToUserDTO(User user);

    @Mapping(target = "password", expression = "java(encodePassword(userDTO.getPassword()))")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "wishlist", source = "wishlist", qualifiedByName = "wishlistDTOToWishlist")
    public abstract User userDTOToUser(UserDTO userDTO);

    @Named("uuidToUser")
    public User uuidToUser(UUID uuid) {
        return userService.getUserByUuid(uuid);
    }

    @Named("rolesToString")
    public String rolesToString(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.joining(","));
    }

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("jsonToProductList")
    public List<CatalogueProductDTO> jsonToProductList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, CatalogueProductDTO.class));
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON to List<CatalogueProductDTO>", e);
        }
    }

    @Named("productListToJson")
    public String productListToJson(List<CatalogueProductDTO> products) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing List<CatalogueProductDTO> to JSON", e);
        }
    }

    @Named("wishlistDTOToWishlist")
    public Wishlist wishlistDTOToWishlist(WishlistDTO wishlistDTO) {
        if (wishlistDTO == null) {
            return null;
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setId(wishlistDTO.getId());
        wishlist.setItems(productListToJson(wishlistDTO.getItems()));
        wishlist.setUser(uuidToUser(wishlistDTO.getUserUuid()));
        return wishlist;
    }

    @Named("wishlistToWishlistDTO")
    public WishlistDTO wishlistToWishlistDTO(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setId(wishlist.getId());
        wishlistDTO.setItems(jsonToProductList(wishlist.getItems()));
        wishlistDTO.setUserUuid(wishlist.getUser().getUuid());
        return wishlistDTO;
    }
}