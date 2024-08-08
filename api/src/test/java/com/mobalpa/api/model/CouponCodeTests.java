package com.mobalpa.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CouponCodeTests {

    @Test
    public void testCouponCodeCreation() {
        CouponCode couponCode = new CouponCode();
        couponCode.setId(1);
        couponCode.setName("SUMMER2024");
        couponCode.setDiscountRate(20.0);
        LocalDateTime now = LocalDateTime.now();
        couponCode.setDateStart(now);
        couponCode.setDateEnd(now.plusDays(10));

        assertNotNull(couponCode);
        assertEquals(1, couponCode.getId());
        assertEquals("SUMMER2024", couponCode.getName());
        assertEquals(20.0, couponCode.getDiscountRate(), 0.001);
        assertEquals(now, couponCode.getDateStart());
        assertEquals(now.plusDays(10), couponCode.getDateEnd());
    }
}