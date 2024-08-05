package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginRequestDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

@RestController
@RequestMapping("api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successful. Please check your email for confirmation.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        User user = userService.confirmUser(token);
        if (user != null) {
            return ResponseEntity.ok("Account confirmed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            User user = userService.getUserByEmail(loginDTO.getEmail());

            if (user == null || !user.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is not active or does not exist");
            }

            String token = userService.generateToken(user);
            return ResponseEntity.ok(new LoginRequestDTO(user, token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while logging in");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User user = userService.getUserByEmail(email);
        if (user == null || !user.isActive()) {
            return ResponseEntity.badRequest().body("User not found or inactive");
        }
        userService.sendPasswordResetEmail(user);
        return ResponseEntity.ok("Password reset email sent.");
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        User user = userService.resetPassword(token, newPassword);
        if (user != null) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or user not found.");
        }
    }
}
