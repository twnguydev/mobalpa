package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mobalpa.api.repository.RoleRepository;
import com.mobalpa.api.repository.UserRepository;

import com.mobalpa.api.dto.delivery.DeliveryDTO;

@DataJpaTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
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

        OrderItem item1 = new OrderItem();
        UUID item1Uuid = UUID.randomUUID();
        item1.setProductUuid(item1Uuid);
        item1.setQuantity(1);

        OrderItem item2 = new OrderItem();
        UUID item2Uuid = UUID.randomUUID();
        item2.setProductUuid(item2Uuid);
        item2.setQuantity(2);

        List<OrderItem> items = List.of(item1, item2);

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress("456 Elm St");
        order.setReduction(0.0);
        order.setTotalHt(100.0);
        order.setDeliveryFees(10.0);
        order.setDeliveryMethod("UPS");
        order.setVat(20.0);
        order.setTotalTtc(130.0);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(items);

        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setOrderUuid(order.getUuid());
        deliveryDTO.setRecipientAddress("10 rue de la Paix, 75002 Paris, France");
        deliveryDTO.setShippingMethodCheckoutName("Chronopost");

        List<DeliveryDTO> deliveries = List.of(deliveryDTO);
        List<String> deliveryNumbers = new ArrayList<>();
        deliveries.forEach(delivery -> {
            deliveryNumbers.add(delivery.getDeliveryNumber());
        });

        order.setDeliveryNumbers(deliveryNumbers);

        user.getOrders().add(order);
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, savedUser.getOrders().size());
        assertEquals("PENDING", savedUser.getOrders().iterator().next().getStatus());
        assertEquals(2, savedUser.getOrders().iterator().next().getItems().size());
        assertEquals(item1Uuid, savedUser.getOrders().iterator().next().getItems().get(0).getProductUuid());
        assertEquals(1, savedUser.getOrders().iterator().next().getItems().get(0).getQuantity());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setFirstname("Charlie");
        user.setLastname("Davis");
        user.setEmail("charlie.davis@example.com");
        user.setPassword("password");
        user.setPhoneNumber("3334445555");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        user.setPassword("newpassword");
        user.setPhoneNumber("4445556666");
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals("newpassword", savedUser.getPassword());
        assertEquals("4445556666", savedUser.getPhoneNumber());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setFirstname("David");
        user.setLastname("Evans");
        user.setEmail("david.evans@example.com");
        user.setPassword("password");
        user.setPhoneNumber("5556667777");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        UUID userId = user.getUuid();
        userRepository.deleteById(userId);

        User savedUser = userRepository.findById(userId).orElse(null);
        assertNull(savedUser);
    }
}