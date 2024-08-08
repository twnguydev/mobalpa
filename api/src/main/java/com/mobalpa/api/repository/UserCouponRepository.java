package com.mobalpa.api.repository;

import com.mobalpa.api.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.User;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {
    UserCoupon findByUserAndCoupon(User user, CouponCode coupon);
}