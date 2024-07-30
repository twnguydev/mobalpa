package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;  
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/users")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.debug("Registering user with email: {}", user.getEmail());
        userService.registerUser(user);
        return ResponseEntity.ok("Registration successful. Please check your email for confirmation.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        logger.debug("Confirming user with token: {}", token);
        User user = userService.confirmUser(token);
        if (user != null) {
            return ResponseEntity.ok("Account confirmed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }

    @PostMapping("/login")
public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) { 
    logger.debug("Logging in user with email: {}", loginDTO.getEmail());
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        User user = userService.getUserByEmail(loginDTO.getEmail());

        if (user == null || !user.isActive()) {
            logger.error("User is not active or does not exist: {}", loginDTO.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is not active or does not exist");
        }

        String token = userService.generateToken(user);
        logger.debug("User logged in successfully, token generated: {}", token);
        return ResponseEntity.ok(token);
    } catch (BadCredentialsException e) {
        logger.error("Invalid credentials for user with email: {}", loginDTO.getEmail());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
    } catch (AuthenticationException e) {
        logger.error("Authentication error for user with email: {}", loginDTO.getEmail(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    } catch (Exception e) {
        logger.error("Error during login for user with email: {}", loginDTO.getEmail(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
}

}
