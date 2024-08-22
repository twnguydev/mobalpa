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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la génération du contenu HTML.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = userService.getUserByEmail(loginDTO.getEmail());
        if (user.getToken() != null) {
            return ResponseEntity.badRequest().body("User account not confirmed.");
        }
        String token = userService.generateToken(user);
        return ResponseEntity.ok(new LoginRequestDTO(user, token));
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
