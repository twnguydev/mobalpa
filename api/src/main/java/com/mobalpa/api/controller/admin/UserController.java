package com.mobalpa.api.controller.admin;

import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;

@RestController("adminUserController")
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "User management for admin users", description = "APIs for managing users for admin users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Fetches all users.")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get user by UUID", description = "Fetches a user by their unique identifier.")
    public ResponseEntity<User> getUserById(@PathVariable UUID uuid) {
        try {
            User user = userService.getUserByUuid(uuid);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user.")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update user", description = "Updates a user.")
    public ResponseEntity<User> updateUser(@PathVariable UUID uuid, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(uuid, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete user", description = "Deletes a user.")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        try {
            userService.deleteUser(uuid);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/details/{uuid}")
    public ResponseEntity<User> findByIdWithDetails(@PathVariable UUID uuid) {
        Optional<User> user = userService.findByIdWithDetails(uuid);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{uuid}/payments")
    public ResponseEntity<List<Payment>> getPaymentsByUserUuid(@PathVariable UUID uuid) {
        List<Payment> payments = paymentService.getPaymentsByUserUuid(uuid);
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
    
}
