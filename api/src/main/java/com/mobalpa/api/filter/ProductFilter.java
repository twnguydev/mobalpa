package com.mobalpa.api.filter;

import java.util.UUID;

public class ProductFilter {
    private Double maxPrice;
    private Double minPrice;
    private String brandName;
    private String colorName;
    private UUID subcategoryId;

    public UUID getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(UUID subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}