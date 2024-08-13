package com.mobalpa.catalogue.mapper;

import com.mobalpa.catalogue.dto.*;
import com.mobalpa.catalogue.model.*;

import java.util.stream.Collectors;

public class Mapper {

    public static SimpleDTO toSimpleDTO(Object entity) {
        if (entity instanceof Category) {
            Category category = (Category) entity;
            SimpleDTO dto = new SimpleDTO();
            dto.setUuid(category.getUuid());
            dto.setName(category.getName());
            dto.setUri(category.getUri());
            return dto;
        } else if (entity instanceof Subcategory) {
            Subcategory subcategory = (Subcategory) entity;
            SimpleDTO dto = new SimpleDTO();
            dto.setUuid(subcategory.getUuid());
            dto.setName(subcategory.getName());
            dto.setUri(subcategory.getUri());
            return dto;
        } else if (entity instanceof Product) {
            Product product = (Product) entity;
            SimpleDTO dto = new SimpleDTO();
            dto.setUuid(product.getUuid());
            dto.setName(product.getName());
            dto.setUri(product.getUri());
            return dto;
        } else if (entity instanceof Brand) {
            Brand brand = (Brand) entity;
            SimpleDTO dto = new SimpleDTO();
            dto.setUuid(brand.getUuid());
            dto.setName(brand.getName());
            dto.setUri(null);
            return dto;
        } else if (entity instanceof Color) {
            Color color = (Color) entity;
            SimpleDTO dto = new SimpleDTO();
            dto.setUuid(color.getUuid());
            dto.setName(color.getName());
            dto.setUri(null);
            return dto;
        }
        return null;
    }

    public static ImageDTO toImageDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setUuid(image.getUuid());
        dto.setUri(image.getUri());
        dto.setColor(toSimpleDTO(image.getColor()));
        return dto;
    }

    public static CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setUuid(category.getUuid());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setUri(category.getUri());
        dto.setSubcategories(category.getSubcategories().stream().map(Mapper::toSimpleDTO).collect(Collectors.toList()));
        return dto;
    }

    public static SubcategoryDTO toSubcategoryDTO(Subcategory subcategory) {
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setUuid(subcategory.getUuid());
        dto.setName(subcategory.getName());
        dto.setDescription(subcategory.getDescription());
        dto.setUri(subcategory.getUri());
        dto.setCategory(toSimpleDTO(subcategory.getCategory()));
        dto.setProducts(subcategory.getProducts().stream().map(Mapper::toSimpleDTO).collect(Collectors.toList()));
        return dto;
    }

    public static ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setUuid(product.getUuid());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setEstimatedDelivery(product.getEstimatedDelivery());
        dto.setWeight(product.getWeight());
        dto.setHeight(product.getHeight());
        dto.setWidth(product.getWidth());
        dto.setUri(product.getUri());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setCategory(toSimpleDTO(product.getCategory()));
        dto.setSubcategory(toSimpleDTO(product.getSubcategory()));
        dto.setBrand(toSimpleDTO(product.getBrand()));
        dto.setColors(product.getColors().stream().map(Mapper::toSimpleDTO).collect(Collectors.toList()));
        dto.setImages(product.getImages().stream().map(Mapper::toImageDTO).collect(Collectors.toList()));
        return dto;
    }
}