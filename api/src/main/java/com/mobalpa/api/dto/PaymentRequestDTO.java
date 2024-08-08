package com.mobalpa.api.dto;

import com.mobalpa.api.model.Payment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentRequestDTO {
    private String cardNumber;
    private LocalDateTime expirationDate;
    private String cvv;
    private String cardHolder;
    private String paypalEmail;
    private Payment.PaymentMethod paymentMethod;
}