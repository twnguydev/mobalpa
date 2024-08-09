package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginRequestDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@RestController
@RequestMapping("api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User registeredUser = userService.registerUser(user);
            response.put("message", "Registration successful. Please check your email for confirmation.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        User user = userService.confirmUser(token);

        try {
            String templatePath;
            if (user != null) {
                templatePath = "src/main/resources/templates/successTemplate.html";
            } else {
                templatePath = "src/main/resources/templates/errorTemplate.html";
            }

            String htmlContent = Files.readString(Paths.get(templatePath));
            return ResponseEntity.ok(htmlContent);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération du contenu HTML.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            User user = userService.getUserByEmail(loginDTO.getEmail());

            if (user == null || !user.isActive()) {
                response.put("error", "Account is not active or does not exist");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            String token = userService.generateToken(user);
            response.put("user", new LoginRequestDTO(user, token));
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (AuthenticationException e) {
            response.put("error", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "An error occurred while logging in");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        String email = body.get("email");
        User user = userService.getUserByEmail(email);

        if (user == null || !user.isActive()) {
            response.put("error", "User not found or inactive");
            return ResponseEntity.badRequest().body(response);
        }

        userService.sendPasswordResetEmail(user);
        response.put("message", "Password reset email sent.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String token, @RequestBody Map<String, String> body) {
    Map<String, Object> response = new HashMap<>();
    String newPassword = body.get("newPassword");
    String confirmPassword = body.get("confirmPassword");
    try {
        User user = userService.resetPassword(token, newPassword, confirmPassword);
        if (user != null) {
            response.put("message", "Password reset successful.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid token or user not found.");
            return ResponseEntity.badRequest().body(response);
        }
    } catch (IllegalArgumentException e) {
        response.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}

}
