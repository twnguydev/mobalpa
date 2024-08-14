package com.mobalpa.api.repository;

import com.mobalpa.api.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  Optional<Payment> findByUuid(UUID uuid);
}