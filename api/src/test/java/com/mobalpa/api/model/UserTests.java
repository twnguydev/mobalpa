package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mobalpa.api.repository.OrderRepository;
import com.mobalpa.api.repository.PaymentRepository;
import com.mobalpa.api.repository.RoleRepository;
import com.mobalpa.api.repository.UserRepository;

@DataJpaTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setPhoneNumber("1234567890");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstname());
        assertEquals("Doe", savedUser.getLastname());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    public void testUserRoles() {
        User user = new User();
        user.setFirstname("Jane");
        user.setLastname("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password");
        user.setPhoneNumber("0987654321");
        user.setCreatedAt(LocalDateTime.now());

        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        user.getRoles().add(role);
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("ROLE_USER", savedUser.getRoles().iterator().next().getName());
    }

    @Test
    public void testUserPayments() {
        User user = new User();
        user.setFirstname("Alice");
        user.setLastname("Smith");
        user.setEmail("alice.smith@example.com");
        user.setPassword("password");
        user.setPhoneNumber("1112223333");
        user.setCreatedAt(LocalDateTime.now());

        Payment payment = new Payment();
        payment.setCardNumber("1234567890123456");
        payment.setExpirationDate(LocalDateTime.now().plusYears(1));
        payment.setCvv("123");
        payment.setCardHolder("Alice Smith");
        payment.setUser(user);

        user.getPayments().add(payment);
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, savedUser.getPayments().size());
        assertEquals("1234567890123456", savedUser.getPayments().iterator().next().getCardNumber());
    }

    @Test
    public void testUserOrders() {
        User user = new User();
        user.setFirstname("Bob");
        user.setLastname("Brown");
        user.setEmail("bob.brown@example.com");
        user.setPassword("password");
        user.setPhoneNumber("2223334444");
        user.setCreatedAt(LocalDateTime.now());

        Order order = new Order();
        order.setItems("{\"item1\": 1, \"item2\": 2}");
        order.setUser(user);
        order.setDeliveryAddress("123 Main St");
        order.setReduction(0L);
        order.setDeliveryFees(500L);
        order.setVat(100L);
        order.setTotalHt(2000L);
        order.setTotalTtc(2600L);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        user.getOrders().add(order);
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, savedUser.getOrders().size());
        assertEquals("PENDING", savedUser.getOrders().iterator().next().getStatus());
    }
}
