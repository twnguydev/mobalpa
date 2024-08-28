package com.mobalpa.api.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ManyToOne(cascade = CascadeType.REMOVE) 
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FKrjgyjb2su0t4uj1pq82g6k9rx"))
    private CouponCode coupon;

    @Column(nullable = false)
    private boolean claimed;

    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateClaimed = null;
}
