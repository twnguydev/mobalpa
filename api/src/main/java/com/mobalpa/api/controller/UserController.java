package com.mobalpa.api.controller;

import com.mobalpa.api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.service.WishlistService;
import com.mobalpa.api.dto.PaymentRequestDTO;
import com.mobalpa.api.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private WishlistService wishlistService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @GetMapping("/{uuid}")
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    try {
      User user = userService.getUserByUuid(uuid);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      User createdUser = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{uuid}")
  public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
    try {
      User updatedUser = userService.updateUser(uuid, user);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @DeleteMapping("/{uuid}")
  public ResponseEntity<?> deleteUser(@PathVariable UUID uuid) {
    userService.deleteUser(uuid);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
  }

  @GetMapping("/{id}/wishlist")
  public ResponseEntity<?> getWishlist(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    Wishlist wishlist = user.getWishlist();
    if (wishlist == null || wishlist.getItems().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no items in his wishlist");
    }

    return ResponseEntity.ok(wishlist);
  }

  @PatchMapping("/{id}/wishlist")
  public ResponseEntity<?> modifyWishlist(@PathVariable UUID id, @RequestBody WishlistDTO request) {
    try {
      Wishlist wishlist;
      switch (request.getAction().toLowerCase()) {
        case "add":
          wishlist = wishlistService.addToWishlist(id, request.getItem());
          break;
        case "remove":
          wishlist = wishlistService.removeFromWishlist(id, request.getItem().getProductId(),
              request.getItem().getQuantity());
          break;
        default:
          return ResponseEntity.badRequest().body("Invalid action for wishlist");
      }
      return ResponseEntity.ok(wishlist);
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(500).body("Error processing wishlist");
    }
  }

  @GetMapping("/{id}/orders")
  public ResponseEntity<?> getOrders(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    if (user.getOrders().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no orders");
    }

    return ResponseEntity.ok(user.getOrders());
  }

  @GetMapping("/{id}/payments")
  public ResponseEntity<?> getPayments(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    if (user.getPayments().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no payments");
    }

    return ResponseEntity.ok(user.getPayments());
  }

  @PostMapping("/{id}/payments")
  public ResponseEntity<?> addPaymentToUser(@PathVariable UUID id, @RequestBody PaymentRequestDTO paymentDTO) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = userOptional.get();

    Payment payment = new Payment();
    payment.setUser(user);
    payment.setCardNumber(paymentDTO.getCardNumber());
    payment.setExpirationDate(paymentDTO.getExpirationDate());
    payment.setCvv(paymentDTO.getCvv());
    payment.setCardHolder(paymentDTO.getCardHolder());
    payment.setPaypalEmail(paymentDTO.getPaypalEmail());
    payment.setPaymentMethod(paymentDTO.getPaymentMethod());

    Payment savedPayment = paymentRepository.save(payment);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
  }
}
