package com.mobalpa.api.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserCouponTests {

    @Test
    public void testUserCouponCreation() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail("test@example.com");

        CouponCode couponCode = new CouponCode();
        couponCode.setId(1);
        couponCode.setName("SUMMER2024");
        couponCode.setDiscountRate(20.0);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(1);
        userCoupon.setUser(user);
        userCoupon.setCoupon(couponCode);
        userCoupon.setClaimed(false);

        assertNotNull(userCoupon);
        assertEquals(1, userCoupon.getId());
        assertEquals(user, userCoupon.getUser());
        assertEquals(couponCode, userCoupon.getCoupon());
        assertFalse(userCoupon.isClaimed());
    }

    @Test
    public void testUserCouponClaimed() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail("test@example.com");

        CouponCode couponCode = new CouponCode();
        couponCode.setId(1);
        couponCode.setName("SUMMER2024");
        couponCode.setDiscountRate(20.0);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(1);
        userCoupon.setUser(user);
        userCoupon.setCoupon(couponCode);
        userCoupon.setClaimed(true);

        assertNotNull(userCoupon);
        assertEquals(1, userCoupon.getId());
        assertEquals(user, userCoupon.getUser());
        assertEquals(couponCode, userCoupon.getCoupon());
        assertTrue(userCoupon.isClaimed());
    }
}
