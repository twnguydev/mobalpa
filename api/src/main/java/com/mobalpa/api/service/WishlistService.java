package com.mobalpa.api.service;

import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.WishlistItem;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.WishlistRepository;
import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.dto.catalogue.ColorDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogueService catalogueService;

    public Wishlist getWishlistByUserUuid(UUID userUuid) {
        Wishlist wishlist = wishlistRepository.findByUserUuid(userUuid);

        if (wishlist == null) {
            Optional<User> userOpt = userRepository.findById(userUuid);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                wishlist = new Wishlist();
                wishlist.setUser(user);
                wishlist = wishlistRepository.save(wishlist);
            } else {
                throw new RuntimeException("User not found");
            }
        }
        return wishlist;
    }

    public Wishlist addToWishlist(UUID userId, WishlistItem newItem) {
        User user = userService.getUserByUuid(userId);
        Wishlist wishlist = getWishlistByUserUuid(userId);
    
        ProductDTO product = catalogueService.getProductById(newItem.getProductUuid());
        if (product == null) {
            throw new RuntimeException("Product with UUID " + newItem.getProductUuid() + " not found");
        }
    
        List<Campaign> campaigns = promotionService.getProductCampaigns(product.getUuid());
    
        if (newItem.getSelectedColor() != null && !product.getColors().stream().map(ColorDTO::getName)
                .collect(Collectors.toList()).contains(newItem.getSelectedColor())) {
            throw new RuntimeException("Color " + newItem.getSelectedColor() + " not available for product with UUID "
                    + newItem.getProductUuid());
        }
    
        List<WishlistItem> items = wishlist.getItems();
        Optional<WishlistItem> existingItemOpt = items.stream()
                .filter(item -> item.getProductUuid().equals(newItem.getProductUuid())
                        && item.getSelectedColor().equals(newItem.getSelectedColor()))
                .findFirst();
    
        Map<String, String> properties = new HashMap<>();
        properties.put("brand", product.getBrand() != null ? product.getBrand().getName() : "Unknown");
        properties.put("images",
                product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0).getUri()
                        : "No image");
    
        if (existingItemOpt.isPresent()) {
            WishlistItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
        } else {
            newItem.setWishlist(wishlist);
            newItem.setProduct(product);
            newItem.setCampaigns(campaigns != null ? campaigns : new ArrayList<>());
            newItem.setProperties(properties);
            items.add(newItem);
        }

        wishlist.setItems(items);
        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        for (WishlistItem item : savedWishlist.getItems()) {
            ProductDTO itemProduct = catalogueService.getProductById(item.getProductUuid());
            item.setProduct(itemProduct);
        }
    
        return savedWishlist;
    }         

    public Wishlist removeFromWishlist(UUID userId, UUID productId, Integer quantity) {
        User user = userService.getUserByUuid(userId);
        Wishlist wishlist = user.getWishlist();

        if (wishlist == null) {
            throw new RuntimeException("Wishlist not found for the user");
        }

        List<WishlistItem> items = wishlist.getItems();
        items.removeIf(item -> {
            if (item.getProductUuid().equals(productId)) {
                if (quantity == null || item.getQuantity() <= quantity) {
                    return true;
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                    return false;
                }
            }
            return false;
        });

        wishlist.setItems(items);
        user.setWishlist(wishlist);
        userRepository.save(user);

        return wishlistRepository.save(wishlist);
    }
}