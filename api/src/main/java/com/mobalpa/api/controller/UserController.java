package com.mobalpa.api.controller;

import com.mobalpa.api.service.CatalogueService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Wishlist;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.WishlistDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Users, customers", description = "APIs for managing users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private WishlistService wishlistService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private CatalogueService catalogueService;

  @GetMapping("/{uuid}")
  @Operation(summary = "Get user by UUID", description = "Fetches a user by their unique identifier.")
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    try {
      User user = userService.getUserByUuid(uuid);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PostMapping
  @Operation(summary = "Create user", description = "Creates a new user.")
  public ResponseEntity<?> createUser(@RequestBody User user) {
    try {
      User createdUser = userService.createUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PatchMapping("/{uuid}")
  @Operation(summary = "Update user", description = "Updates an existing user.")
  public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
    try {
      System.out.println("User: " + user);
      User updatedUser = userService.updateUser(uuid, user);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Delete user", description = "Deletes a user.")
  public ResponseEntity<?> deleteUser(@PathVariable UUID uuid) {
    userService.deleteUser(uuid);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
  }

  @GetMapping("/{id}/wishlist")
  @Operation(summary = "Get user's wishlist", description = "Fetches a user's wishlist.")
  public ResponseEntity<?> getWishlist(@PathVariable UUID id) {
    User user = userService.getUserByUuid(id);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    Wishlist wishlist = user.getWishlist();
    if (wishlist == null || wishlist.getItems().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no items in his wishlist");
    }

    wishlist.getItems().forEach(item -> {
      ProductDTO product = catalogueService.getProductById(item.getProductUuid());
      if (product != null) {
        item.setProduct(product);
      }
    });

    return ResponseEntity.ok(wishlist);
  }

  @PatchMapping("/{id}/wishlist")
  @Operation(summary = "Modify user's wishlist", description = "Modifies a user's wishlist.")
  public ResponseEntity<?> modifyWishlist(@PathVariable UUID id, @RequestBody WishlistDTO request) {
    Wishlist wishlist;
    switch (request.getAction().toLowerCase()) {
      case "add":
        wishlist = wishlistService.addToWishlist(id, request.getItem());
        break;
      case "remove":
        wishlist = wishlistService.removeFromWishlist(id, request.getItem().getProductUuid(),
            request.getItem().getQuantity());
        break;
      default:
        return ResponseEntity.badRequest().body("Invalid action for wishlist");
    }
    return ResponseEntity.ok(wishlist);
  }

  @GetMapping("/{id}/orders")
  @Operation(summary = "Get user's orders", description = "Fetches a user's orders.")
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
  @Operation(summary = "Get user's payments", description = "Fetches a user's payments.")
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
  @Operation(summary = "Add payment to user", description = "Adds a payment to a user.")
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

  @DeleteMapping("/{id}/payments/{paymentId}")
  @Operation(summary = "Delete payment", description = "Deletes a payment.")
  public ResponseEntity<?> deletePayment(@PathVariable UUID id, @PathVariable UUID paymentId) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = userOptional.get();
    Optional<Payment> paymentOptional = user.getPayments().stream().filter(p -> p.getUuid().equals(paymentId)).findFirst();
    if (paymentOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
    }

    Payment payment = paymentOptional.get();
    paymentRepository.delete(payment);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payment deleted");
  }
}
