package com.mobalpa.api.service;

import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.WishlistItem;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.WishlistRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    public Wishlist getWishlistByUserUuid(UUID userUuid) {
        Wishlist wishlist = wishlistRepository.findByUserUuid(userUuid);

        if (wishlist == null) {
            User user = userService.getUserByUuid(userUuid);

            if (user != null) {
                Optional<Wishlist> wishlistOptional = Optional.ofNullable(user.getWishlist());

                if (wishlistOptional.isPresent()) {
                    return wishlistOptional.get();
                } else {
                    wishlist = new Wishlist();
                    wishlist.setUser(user);
                    wishlist.setItems("[]");
                    user.setWishlist(wishlist);
                    wishlist = wishlistRepository.save(wishlist);
                }
            } else {
                throw new RuntimeException("User not found");
            }
        }
        return wishlist;
    }

    public Wishlist addToWishlist(UUID userId, WishlistItem newItem) throws JsonProcessingException {
        User user = userService.getUserByUuid(userId);
        Wishlist wishlist = user.getWishlist();

        List<WishlistItem> items = objectMapper.readValue(wishlist.getItems(), new TypeReference<List<WishlistItem>>() {
        });
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
        User user = userService.getUserByUuid(userId);
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
