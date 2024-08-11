package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mobalpa.api.dto.OrderRequestDTO;
import com.mobalpa.api.repository.PaymentRepository;
import com.mobalpa.api.repository.RoleRepository;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.service.OrderService;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private OrderService orderService;

    @Autowired
    private PaymentRepository paymentRepository;

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
        // Création du User
        User user = new User();
        user.setFirstname("Bob");
        user.setLastname("Brown");
        user.setEmail("bob.brown@example.com");
        user.setPassword("password");
        user.setPhoneNumber("2223334444");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Création du Payment
        Payment payment = new Payment();
        payment.setCardNumber("1234567890123456");
        payment.setExpirationDate(LocalDateTime.now().plusYears(1));
        payment.setCvv("123");
        payment.setCardHolder("Bob Brown");
        payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        payment.setUser(user);
        paymentRepository.save(payment);

        // Création des OrderItems
        OrderItem item1 = new OrderItem();
        item1.setProductUuid(UUID.randomUUID());
        item1.setQuantity(1);

        OrderItem item2 = new OrderItem();
        item2.setProductUuid(UUID.randomUUID());
        item2.setQuantity(2);

        List<OrderItem> items = List.of(item1, item2);

        // Création de l'OrderRequestDTO
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setUserUuid(user.getUuid());
        orderRequestDTO.setPaymentUuid(payment.getUuid());
        orderRequestDTO.setItems(items);
        orderRequestDTO.setTotalHt(100.0);
        orderRequestDTO.setDeliveryMethod("UPS");
        orderRequestDTO.setDeliveryAddress("456 Elm St");

        // Appel de la méthode createOrder pour créer la commande
        Order order = orderService.createOrder(orderRequestDTO);

        // Vérification des résultats
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals("PENDING", order.getStatus());
        assertEquals(2, order.getItems().size());
        assertEquals(100.0, order.getTotalHt());
        assertEquals(1, order.getItems().get(0).getQuantity());
        assertEquals(2, order.getItems().get(1).getQuantity());
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