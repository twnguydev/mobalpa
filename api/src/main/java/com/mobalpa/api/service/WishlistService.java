package com.mobalpa.api.service;

import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserService userService;

    public Wishlist getWishlistByUserUuid(UUID userUuid) {
        // Tente de récupérer la liste de souhaits par UUID utilisateur
        Wishlist wishlist = wishlistRepository.findByUserUuid(userUuid);
        
        // Si aucune liste de souhaits n'est trouvée, créez-en une nouvelle
        if (wishlist == null) {
            // Récupérez l'utilisateur correspondant à l'UUID
            Optional<User> userOptional = userService.getUserByUuid(userUuid);
            
            // Vérifiez si l'utilisateur est présent
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                // Créez une nouvelle liste de souhaits pour l'utilisateur
                wishlist = new Wishlist();
                wishlist.setUser(user);
                
                // Enregistrez la nouvelle liste de souhaits
                wishlist = wishlistRepository.save(wishlist);
            } else {
                throw new RuntimeException("User not found"); // Gérez l'absence d'utilisateur comme vous le souhaitez
            }
        }
        return wishlist;
    }

    public Wishlist createOrUpdateWishlist(Wishlist wishlistDTO) {
        // Enregistrez ou mettez à jour la liste de souhaits
        return wishlistRepository.save(wishlistDTO);
    }
}
