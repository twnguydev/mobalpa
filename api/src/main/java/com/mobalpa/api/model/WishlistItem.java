package com.mobalpa.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WishlistItem {

    private String productId;
    private String productName;
    private Integer quantity;

    public WishlistItem(String productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }
}