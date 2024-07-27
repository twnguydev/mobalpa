package com.mobalpa.api.repository;

import com.mobalpa.api.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}