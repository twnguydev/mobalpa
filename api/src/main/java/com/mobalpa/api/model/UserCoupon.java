package com.mobalpa.api.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_coupon")
@Data
@NoArgsConstructor
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private CouponCode coupon;

    @Column(nullable = false)
    private boolean claimed;

    @Column(nullable = false)
    private LocalDate dateClaimed = LocalDate.now();
}