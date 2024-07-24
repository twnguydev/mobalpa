package com.mobalpa.api.service;

import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.mapper.UserMapper;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistService {

  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private UserMapper userMapper;

  public WishlistDTO getWishlistByUserUuid(UUID userUuid) {
    Wishlist wishlist = wishlistRepository.findByUserUuid(userUuid);
    return userMapper.wishlistToWishlistDTO(wishlist);
  }

  public WishlistDTO createOrUpdateWishlist(WishlistDTO wishlistDTO) {
    Wishlist wishlist = userMapper.wishlistDTOToWishlist(wishlistDTO);
    wishlist = wishlistRepository.save(wishlist);
    return userMapper.wishlistToWishlistDTO(wishlist);
  }
}