package com.mobalpa.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.WishlistItem;
import com.mobalpa.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public User getUserByUuid(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, User user) {
        return userRepository.findById(uuid).map(existingUser -> {
            if (user.getFirstname() != null) existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null) existingUser.setPassword(user.getPassword());
            if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getBirthdate() != null) existingUser.setBirthdate(user.getBirthdate());
            if (user.getToken() != null) existingUser.setToken(user.getToken());
            if (user.getZipcode() != null) existingUser.setZipcode(user.getZipcode());
            if (user.getCity() != null) existingUser.setCity(user.getCity());
            if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
            if (user.getUpdatedAt() != null) existingUser.setUpdatedAt(user.getUpdatedAt());
            if (user.getRoles() != null) existingUser.setRoles(user.getRoles());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User with id " + uuid + " not found"));
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }


    public Wishlist addToWishlist(UUID userId, WishlistItem newItem) throws JsonProcessingException {
        User user = getUserByUuid(userId);
        Wishlist wishlist = user.getWishlist();

        List<WishlistItem> items = objectMapper.readValue(wishlist.getItems(), new TypeReference<List<WishlistItem>>() {});
        boolean itemExists = false;

        for (WishlistItem item : items) {
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            items.add(newItem);
        }

        wishlist.setItems(objectMapper.writeValueAsString(items));
        user.setWishlist(wishlist);
        userRepository.save(user);

        return wishlist;
    }

    public Wishlist removeFromWishlist(UUID userId, String productId, Integer quantity) throws JsonProcessingException {
        User user = getUserByUuid(userId);
        Wishlist wishlist = user.getWishlist();

        List<WishlistItem> items = objectMapper.readValue(wishlist.getItems(), new TypeReference<List<WishlistItem>>() {});
        items.removeIf(item -> {
            if (item.getProductId().equals(productId)) {
                if (quantity == null || item.getQuantity() <= quantity) {
                    return true;
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                    return false;
                }
            }
            return false;
        });

        wishlist.setItems(objectMapper.writeValueAsString(items));
        user.setWishlist(wishlist);
        userRepository.save(user);

        return wishlist;
    }
}