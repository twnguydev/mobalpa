package com.mobalpa.api.controller;

import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.dto.PaymentRequestDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.model.WishlistItem;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.repository.PaymentRepository;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.WishlistService;
import com.mobalpa.api.service.CatalogueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CatalogueService catalogueService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByUuidSuccess() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByUuid(userUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserByUuidNotFound() {
        UUID userUuid = UUID.randomUUID();

        when(userService.getUserByUuid(any(UUID.class))).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = userController.getUserByUuid(userUuid);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testCreateUserSuccess() {
        User user = new User();

        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testCreateUserBadRequest() {
        User user = new User();

        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid user data"));

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid user data", response.getBody());
    }

    @Test
    void testUpdateUserSuccess() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();
        User updatedUser = new User();

        when(userService.updateUser(any(UUID.class), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(userUuid, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testUpdateUserBadRequest() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();

        when(userService.updateUser(any(UUID.class), any(User.class)))
            .thenThrow(new IllegalArgumentException("Invalid user data"));

        ResponseEntity<?> response = userController.updateUser(userUuid, user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid user data", response.getBody());
    }

    @Test
    void testUpdateUserNotFound() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();

        when(userService.updateUser(any(UUID.class), any(User.class)))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = userController.updateUser(userUuid, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testDeleteUserSuccess() {
        UUID userUuid = UUID.randomUUID();

        doNothing().when(userService).deleteUser(any(UUID.class));

        ResponseEntity<?> response = userController.deleteUser(userUuid);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("User deleted", response.getBody());
    }

    @Test
    void testGetWishlistSuccess() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        Wishlist wishlist = new Wishlist();
        wishlist.setItems(List.of(new WishlistItem()));

        ProductDTO productDTO = new ProductDTO();
        when(catalogueService.getProductById(any(UUID.class))).thenReturn(productDTO);

        user.setWishlist(wishlist);
        when(userService.getUserByUuid(any(UUID.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getWishlist(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlist, response.getBody());
    }

    @Test
    void testGetWishlistNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(null);

        ResponseEntity<?> response = userController.getWishlist(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testModifyWishlistAddSuccess() {
        UUID userId = UUID.randomUUID();
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setAction("add");
        WishlistItem item = new WishlistItem();
        item.setProductUuid(UUID.randomUUID());
        wishlistDTO.setItem(item);
    
        when(wishlistService.addToWishlist(any(UUID.class), any(WishlistItem.class))).thenReturn(new Wishlist());
    
        ResponseEntity<?> response = userController.modifyWishlist(userId, wishlistDTO);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }    

    @Test
    void testModifyWishlistRemoveSuccess() {
        UUID userId = UUID.randomUUID();
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setAction("remove");
        WishlistItem item = new WishlistItem();
        item.setProductUuid(UUID.randomUUID());
        item.setQuantity(1);
        wishlistDTO.setItem(item);
    
        when(wishlistService.removeFromWishlist(any(UUID.class), any(UUID.class), anyInt())).thenReturn(new Wishlist());
    
        ResponseEntity<?> response = userController.modifyWishlist(userId, wishlistDTO);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }    

    @Test
    void testModifyWishlistBadRequest() {
        UUID userId = UUID.randomUUID();
        WishlistDTO request = new WishlistDTO();
        request.setAction("invalid");

        ResponseEntity<?> response = userController.modifyWishlist(userId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid action for wishlist", response.getBody());
    }

    @Test
    void testGetOrdersSuccess() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setOrders(List.of(new Order()));

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getOrders(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getOrders(), response.getBody());
    }

    @Test
    void testGetOrdersNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(null);

        ResponseEntity<?> response = userController.getOrders(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testGetPaymentsSuccess() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setPayments(Set.of(new Payment()));

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getPayments(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getPayments(), response.getBody());
    }

    @Test
    void testGetPaymentsNotFound() {
        UUID userId = UUID.randomUUID();

        when(userService.getUserByUuid(any(UUID.class))).thenReturn(null);

        ResponseEntity<?> response = userController.getPayments(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testAddPaymentToUserSuccess() {
        UUID userId = UUID.randomUUID();
        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
        Payment payment = new Payment();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        ResponseEntity<?> response = userController.addPaymentToUser(userId, paymentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(payment, response.getBody());
    }

    @Test
    void testAddPaymentToUserNotFound() {
        UUID userId = UUID.randomUUID();
        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.addPaymentToUser(userId, paymentDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testDeletePaymentSuccess() {
        UUID userId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        User user = new User();
        Payment payment = new Payment();
        payment.setUuid(paymentId);
        user.setPayments(Set.of(payment));
    
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(paymentRepository.findById(any(UUID.class))).thenReturn(Optional.of(payment));
    
        ResponseEntity<?> response = userController.deletePayment(userId, paymentId);
    
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(paymentRepository, times(1)).delete(payment);
    }    

    @Test
    void testDeletePaymentUserNotFound() {
        UUID userId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.deletePayment(userId, paymentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testDeletePaymentPaymentNotFound() {
        UUID userId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(paymentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.deletePayment(userId, paymentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Payment not found", response.getBody());
    }
}