package com.mobalpa.api.repository;

import com.mobalpa.api.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}