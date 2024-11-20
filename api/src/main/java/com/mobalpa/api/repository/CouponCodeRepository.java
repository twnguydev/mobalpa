package com.mobalpa.api.repository;

import com.mobalpa.api.model.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponCodeRepository extends JpaRepository<CouponCode, Integer> {
  Optional<CouponCode> findByName(String code);
}