package com.mobalpa.api.catalogue.repository;

import com.mobalpa.api.catalogue.model.Product;
import com.mobalpa.api.catalogue.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findProductsByIds(List<String> ids);

    List<Product> getBestSellers();
}