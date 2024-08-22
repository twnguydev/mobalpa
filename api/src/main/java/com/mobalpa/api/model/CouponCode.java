package com.mobalpa.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coupon_code")
@Data
@NoArgsConstructor
public class CouponCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double discountRate;

    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateStart;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateEnd;

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserCoupon> userCoupons = new HashSet<>();

    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private Integer maxUse;

    @Column(nullable = false)
    private Integer currentUse = 0;

    private List<String> targetUsers;

    public enum TargetType {
        ALL_USERS,
        SPECIFIC_USERS,
        USER
    }

    public enum DiscountType {
        PERCENTAGE,
        AMOUNT
    }
}