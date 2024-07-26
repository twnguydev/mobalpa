package com.mobalpa.api.service;

import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistService {

  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private UserService userService;

  public Wishlist getWishlistByUserUuid(UUID userUuid) {
    Wishlist wishlist = wishlistRepository.findByUserUuid(userUuid);
    if (wishlist == null) {
      User user = userService.getUserByUuid(userUuid);

      wishlist = new Wishlist();
      wishlist.setUser(user);
      wishlist = wishlistRepository.save(wishlist);
    }
    return wishlist;
  }

  public Wishlist createOrUpdateWishlist(Wishlist wishlistDTO) {
    return wishlistRepository.save(wishlistDTO);
  }
}