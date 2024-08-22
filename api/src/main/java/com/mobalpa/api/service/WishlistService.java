package com.mobalpa.api.service;

import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.WishlistItem;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.WishlistRepository;
import com.mobalpa.api.repository.CampaignRepository;
import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.service.PromotionService;
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
    private CampaignRepository campaignRepository;

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
    
        // Récupération du produit depuis le catalogue
        ProductDTO product = catalogueService.getProductById(newItem.getProductUuid());
        if (product == null) {
            throw new RuntimeException("Product with UUID " + newItem.getProductUuid() + " not found");
        }
    
        // Récupération des campagnes associées au produit
        List<Campaign> campaigns = promotionService.getProductCampaigns(newItem.getProductUuid());
    
        if (campaigns != null && !campaigns.isEmpty()) {
            System.out.println("Campaigns found: " + campaigns);
        } else {
            System.out.println("No campaigns found for product UUID: " + newItem.getProductUuid());
        }
    
        // Vérification de la couleur sélectionnée
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
            // Mise à jour de l'item existant
            WishlistItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
            existingItem.setCampaigns(campaigns != null ? campaigns : existingItem.getCampaigns()); 
        } else {
            // Ajout d'un nouvel item à la wishlist
            newItem.setWishlist(wishlist);
            newItem.setCampaigns(campaigns != null ? campaigns : new ArrayList<>());
            newItem.setProperties(properties);
            items.add(newItem);
        }
    
        wishlist.setItems(items);
        return wishlistRepository.save(wishlist);
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