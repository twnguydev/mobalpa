package com.mobalpa.catalogue.service;

import com.mobalpa.catalogue.model.Product;
import com.mobalpa.catalogue.repository.ProductRepository;
import com.mobalpa.catalogue.repository.CategoryRepository;
import com.mobalpa.catalogue.repository.SubcategoryRepository;
import com.mobalpa.catalogue.repository.BrandRepository;
import com.mobalpa.catalogue.repository.ColorRepository;
import com.mobalpa.catalogue.repository.ImageRepository;
import com.mobalpa.catalogue.repository.StoreRepository;
import com.mobalpa.catalogue.model.Category;
import com.mobalpa.catalogue.model.Subcategory;
import com.mobalpa.catalogue.model.Brand;
import com.mobalpa.catalogue.model.Color;
import com.mobalpa.catalogue.model.Image;
import com.mobalpa.catalogue.model.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StoreRepository storeRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(UUID id, Product product) {
        product.setUuid(id);
        return productRepository.save(product);
    }

    public Product createProduct(Product product) {
        if (product.getCategory() != null) {
            Optional<Category> category = categoryRepository.findByName(product.getCategory().getName());
            if (!category.isPresent()) {
                categoryRepository.save(product.getCategory());
            } else {
                product.setCategory(category.get());
            }
        }
        if (product.getSubcategory() != null) {
            Optional<Subcategory> subcategory = subcategoryRepository.findByName(product.getSubcategory().getName());
            if (!subcategory.isPresent()) {
                subcategoryRepository.save(product.getSubcategory());
            } else {
                product.setSubcategory(subcategory.get());
            }
        }
        if (product.getBrand() != null) {
            Optional<Brand> brand = brandRepository.findByName(product.getBrand().getName());
            if (!brand.isPresent()) {
                brandRepository.save(product.getBrand());
            } else {
                product.setBrand(brand.get());
            }
        }
        if (product.getColors() != null) {
            List<Color> colors = product.getColors();
            for (int i = 0; i < colors.size(); i++) {
                Color color = colors.get(i);
                Optional<Color> existingColor = colorRepository.findByName(color.getName());
                if (!existingColor.isPresent()) {
                    colorRepository.save(color);
                } else {
                    colors.set(i, existingColor.get());
                }
            }
        }
        if (product.getImages() != null) {
            List<Image> images = product.getImages();
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                Optional<Image> existingImage = imageRepository.findByUri(image.getUri());
                if (!existingImage.isPresent()) {
                    imageRepository.save(image);
                } else {
                    images.set(i, existingImage.get());
                }
            }
        }
        if (product.getStores() != null) {
            List<Store> stores = product.getStores();
            for (int i = 0; i < stores.size(); i++) {
                Store store = stores.get(i);
                Optional<Store> existingStore = storeRepository.findByName(store.getName());
                if (!existingStore.isPresent()) {
                    storeRepository.save(store);
                } else {
                    stores.set(i, existingStore.get());
                }
            }
        }
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}