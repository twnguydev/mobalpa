package com.mobalpa.api.model;

import com.mobalpa.api.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserPaymentTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserPaymentRelation() {
        User user = new User();
        user.setFirstname("test");
        user.setLastname("user");
        user.setPhoneNumber("+33612345678");
        user.setPassword("password");
        user.setEmail("test@example.com");

        Payment payment1 = new Payment();
        payment1.setCardNumber("1111222233334444");
        payment1.setExpirationDate(LocalDateTime.now().plusYears(1));
        payment1.setCvv("123");
        payment1.setCardHolder("Test User");
        payment1.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        payment1.setUser(user);

        Payment payment2 = new Payment();
        payment2.setCardNumber("5555666677778888");
        payment2.setExpirationDate(LocalDateTime.now().plusYears(2));
        payment2.setCvv("456");
        payment2.setCardHolder("Test User");
        payment2.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        payment2.setUser(user);

        Payment paymentPaypal = new Payment();
        paymentPaypal.setCardHolder("Test User");
        paymentPaypal.setPaypalEmail("test@example.com");
        paymentPaypal.setPaymentMethod(Payment.PaymentMethod.PAYPAL);
        paymentPaypal.setUser(user);

        user.setPayments(Set.of(payment1, payment2, paymentPaypal));

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(3, savedUser.getPayments().size());
    }
}