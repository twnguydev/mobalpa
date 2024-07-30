package com.mobalpa.api.service;

import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public Optional<User> getUserByUuid(UUID uuid) {
        logger.debug("Fetching user by UUID: {}", uuid);
        return userRepository.findById(uuid);
    }

    public User registerUser(User user) {
        logger.debug("Registering new user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);
        userRepository.save(user);
        sendConfirmationEmail(user);
        return user;
    }

    public void sendConfirmationEmail(User user) {
        logger.debug("Sending confirmation email to: {}", user.getEmail());
        String confirmationUrl = appBaseUrl + "/api/users/confirm?token=" + user.getToken();
        String message = "Please confirm your email by clicking the link below:\n" + confirmationUrl;
        emailService.sendEmail(user.getEmail(), "Email Confirmation", message);
    }

    public User confirmUser(String token) {
        logger.debug("Confirming user with token: {}", token);
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setActive(true);
            user.setToken(null);
            userRepository.save(user);
        }
        return user;
    }

    public String generateToken(User user) {
        logger.debug("Generating token for user with email: {}", user.getEmail());
        return jwtUtil.generateToken(user);
    }

    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
}
