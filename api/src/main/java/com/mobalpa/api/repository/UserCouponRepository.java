package com.mobalpa.api.repository;

import com.mobalpa.api.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.User;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {
    void deleteByCoupon(CouponCode couponCode);
    Optional<UserCoupon> findByUserAndCoupon(User user, CouponCode coupon);
    void deleteByCouponId(Integer couponId);

    List<UserCoupon> findByUser(User user);

}