package com.mobalpa.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@EqualsAndHashCode(exclude = "user")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @Column(nullable = true)
    private String cardNumber;

    @Column(nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Column(nullable = true)
    private String cvv;

    @Column(nullable = true)
    private String cardHolder;

    @Column(nullable = true)
    private String paypalEmail;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CREDIT_CARD,
        PAYPAL
    }

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}