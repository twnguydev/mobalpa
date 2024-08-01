package com.mobalpa.catalogue.filter;

import com.mobalpa.catalogue.repository.BrandRepository;
import com.mobalpa.catalogue.repository.ColorRepository;
import com.mobalpa.catalogue.model.Brand;
import com.mobalpa.catalogue.model.Color;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Optional;

public class ProductFilter {
    private Double maxPrice;
    private Double minPrice;
    private String brandName;
    private String colorName;

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

    public Criteria toCriteria(BrandRepository brandRepository, ColorRepository colorRepository) {
        Criteria criteria = new Criteria();

        if (maxPrice != null) {
            criteria.and("price").lte(maxPrice);
        }

        if (minPrice != null) {
            criteria.and("price").gte(minPrice);
        }

        if (brandName != null) {
            Optional<Brand> brand = brandRepository.findByName(brandName);
            if (brand.isPresent()) {
                criteria.and("brand.uuid").is(brand.get().getUuid());
            } else {
                return new Criteria().and("brand.uuid").is(null);
            }
        }

        if (colorName != null) {
            Optional<Color> color = colorRepository.findByName(colorName);
            if (color.isPresent()) {
                criteria.and("colors.uuid").is(color.get().getUuid());
            } else {
                return new Criteria().and("colors.uuid").is(null);
            }
        }

        return criteria;
    }
}