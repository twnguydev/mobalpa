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
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (!existingProductOpt.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Product existingProduct = existingProductOpt.get();

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getStock() != null) {
            existingProduct.setStock(product.getStock());
        }
        if (product.getEstimatedDelivery() != null) {
            existingProduct.setEstimatedDelivery(product.getEstimatedDelivery());
        }
        if (product.getWeight() != null) {
            existingProduct.setWeight(product.getWeight());
        }
        if (product.getHeight() != null) {
            existingProduct.setHeight(product.getHeight());
        }
        if (product.getWidth() != null) {
            existingProduct.setWidth(product.getWidth());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }
        if (product.getSubcategory() != null) {
            existingProduct.setSubcategory(product.getSubcategory());
        }
        if (product.getBrand() != null) {
            existingProduct.setBrand(product.getBrand());
        }
        if (product.getColors() != null) {
            existingProduct.setColors(product.getColors());
        }
        if (product.getImages() != null) {
            existingProduct.setImages(product.getImages());
        }
        if (product.getStores() != null) {
            existingProduct.setStores(product.getStores());
        }

        return productRepository.save(existingProduct);
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