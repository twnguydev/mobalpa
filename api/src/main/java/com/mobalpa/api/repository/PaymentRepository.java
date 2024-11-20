package com.mobalpa.api.repository;

import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByUuid(UUID uuid);
    
    List<Payment> findByUserUuid(UUID userUuid);
}
