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

import com.mobalpa.catalogue.filter.ProductFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Product> getAllProducts(ProductFilter productFilter) {
        Query query = new Query();
        Criteria criteria = productFilter.toCriteria(brandRepository, colorRepository);
        query.addCriteria(criteria);

        return mongoTemplate.find(query, Product.class);
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }
    
    public Product updateProduct(UUID id, Product product) {
        return productRepository.findById(id).map(existingProduct -> {
            if (product.getName() != null) existingProduct.setName(product.getName());
            if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
            if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());
            if (product.getStock() != null) existingProduct.setStock(product.getStock());
            if (product.getWeight() != null) existingProduct.setWeight(product.getWeight());
            if (product.getHeight() != null) existingProduct.setHeight(product.getHeight());
            if (product.getWidth() != null) existingProduct.setWidth(product.getWidth());
            if (product.getCategory() != null) existingProduct.setCategory(product.getCategory());
            if (product.getSubcategory() != null) existingProduct.setSubcategory(product.getSubcategory());
            if (product.getBrand() != null) existingProduct.setBrand(product.getBrand());
            if (product.getColors() != null) existingProduct.setColors(product.getColors());
            if (product.getImages() != null) existingProduct.setImages(product.getImages());

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public Product createProduct(Product product) {
        if (product.getName() == null) {
            throw new RuntimeException("Name is required");
        }
        if (product.getDescription() == null) {
            throw new RuntimeException("Description is required");
        }
        if (product.getPrice() == null) {
            throw new RuntimeException("Price is required");
        }
        if (product.getStock() == null) {
            throw new RuntimeException("Stock is required");
        }
        if (product.getWeight() == null) {
            throw new RuntimeException("Weight is required");
        }
        if (product.getHeight() == null) {
            throw new RuntimeException("Height is required");
        }
        if (product.getWidth() == null) {
            throw new RuntimeException("Width is required");
        }

        if (product.getCategory() != null) {
            Optional<Category> category = categoryRepository.findByName(product.getCategory().getName());
            if (!category.isPresent()) {
                categoryRepository.save(product.getCategory());
            } else {
                product.setCategory(category.get());
            }
        } else {
            throw new RuntimeException("Category is required");
        }

        if (product.getSubcategory() != null) {
            Optional<Subcategory> subcategory = subcategoryRepository.findByName(product.getSubcategory().getName());
            if (!subcategory.isPresent()) {
                Subcategory newSubcategory = product.getSubcategory();
                newSubcategory.setCategory(product.getCategory());

                if (product.getCategory() != null) {
                    Category category = product.getCategory();
                    if (category.getSubcategories() == null) {
                        category.setSubcategories(new ArrayList<>());
                    }
                    category.getSubcategories().add(newSubcategory);
                    categoryRepository.save(category);
                }

                subcategoryRepository.save(newSubcategory);
            } else {
                if (!subcategory.get().getCategory().getUuid().equals(product.getCategory().getUuid())) {
                    throw new RuntimeException("This subcategory does not belong to the category: " + product.getCategory().getName());
                }
                if (product.getCategory() == null) {
                    product.setCategory(subcategory.get().getCategory());
                }
                product.setSubcategory(subcategory.get());
            }

            Subcategory subcategoryEntity = product.getSubcategory();
            if (subcategoryEntity.getProducts() == null) {
                subcategoryEntity.setProducts(new ArrayList<>());
            }
            subcategoryEntity.getProducts().add(product);
            subcategoryRepository.save(subcategoryEntity);
        } else {
            throw new RuntimeException("Subcategory is required");
        }

        if (product.getBrand() != null) {
            Optional<Brand> brand = brandRepository.findByName(product.getBrand().getName());
            if (!brand.isPresent()) {
                brandRepository.save(product.getBrand());
            } else {
                product.setBrand(brand.get());
            }
        } else {
            throw new RuntimeException("Brand is required");
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
        } else {
            throw new RuntimeException("Colors are required");
        }

        if (product.getImages() != null) {
            List<Image> images = product.getImages();
            List<Color> availableColors = product.getColors();
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                if (image.getColor() != null) {
                    Optional<Color> imageColor = availableColors.stream()
                            .filter(c -> c.getName().equals(image.getColor().getName()))
                            .findFirst();

                    if (imageColor.isPresent()) {
                        image.setColor(imageColor.get());
                    } else {
                        throw new RuntimeException("This color does not exist for the product: " + image.getColor().getName() + " for image " + image.getUri());
                    }
                }
                imageRepository.save(image);
            }
        } else {
            throw new RuntimeException("Images are required");
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