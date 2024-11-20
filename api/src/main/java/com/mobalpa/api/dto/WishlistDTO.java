package com.mobalpa.api.dto;

import lombok.Data;

import com.mobalpa.api.model.WishlistItem;

@Data
public class WishlistDTO {
  
  private String action;
  private WishlistItem item;
}