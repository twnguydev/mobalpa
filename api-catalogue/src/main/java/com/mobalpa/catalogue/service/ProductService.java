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

import java.util.ArrayList;
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
        List<String> errors = new ArrayList<>();

        if (product.getCategory() != null) {
            Optional<Category> category = categoryRepository.findByName(product.getCategory().getName());
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                errors.add("Category not found: " + product.getCategory().getName());
            }
        }

        if (product.getSubcategory() != null) {
            Optional<Subcategory> subcategory = subcategoryRepository.findByName(product.getSubcategory().getName());
            if (subcategory.isPresent()) {
                if (product.getCategory() != null) {
                    subcategory.get().setCategory(product.getCategory());
                }
                product.setSubcategory(subcategory.get());
            } else {
                errors.add("Subcategory not found: " + product.getSubcategory().getName());
            }
        }

        if (product.getBrand() != null) {
            Optional<Brand> brand = brandRepository.findByName(product.getBrand().getName());
            if (brand.isPresent()) {
                product.setBrand(brand.get());
            } else {
                errors.add("Brand not found: " + product.getBrand().getName());
            }
        }

        if (product.getColors() != null) {
            List<Color> colors = product.getColors();
            for (int i = 0; i < colors.size(); i++) {
                Color color = colors.get(i);
                Optional<Color> existingColor = colorRepository.findByName(color.getName());
                if (existingColor.isPresent()) {
                    colors.set(i, existingColor.get());
                } else {
                    errors.add("This color does not exist: " + color.getName());
                }
            }
        }

        if (product.getImages() != null) {
            List<Image> images = product.getImages();
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                if (image.getColor() != null) {
                    Optional<Color> existingColor = colorRepository.findByName(image.getColor().getName());
                    if (existingColor.isPresent()) {
                        image.setColor(existingColor.get());
                        images.set(i, image);
                    } else {
                        errors.add("This color does not exist: " + image.getColor().getName() + " for image " + image.getUri());
                    }
                }
            }
        }

        if (product.getStores() != null) {
            List<Store> stores = product.getStores();
            for (int i = 0; i < stores.size(); i++) {
                Store store = stores.get(i);
                Optional<Store> existingStore = storeRepository.findByName(store.getName());
                if (existingStore.isPresent()) {
                    stores.set(i, existingStore.get());
                } else {
                    errors.add("This store does not exist: " + store.getName());
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Product creation failed with the following errors: " + String.join(", ", errors));
        }

        return productRepository.save(product);
    }

    public List<Product> getProductsByStoreId(UUID id) {
        return storeRepository.findProductsByUuid(id);
    }

    public List<Product> getProductsByCategoryId(UUID id) {
        return productRepository.findByCategoryUuid(id);
    }

    public List<Product> getProductsBySubcategoryId(UUID id) {
        return productRepository.findBySubcategoryUuid(id);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}