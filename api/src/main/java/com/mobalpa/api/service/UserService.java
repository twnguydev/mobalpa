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

import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

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
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public Optional<User> getUserByUuid(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);
        userRepository.save(user);
        sendConfirmationEmail(user);
        return user;
    }

    public void sendConfirmationEmail(User user) {
        String confirmationUrl = appBaseUrl + "/api/users/confirm?token=" + user.getToken();
        String message = "Please confirm your email by clicking the link below:\n" + confirmationUrl;
        emailService.sendEmail(user.getEmail(), "Email Confirmation", message);
    }

    public User confirmUser(String token) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setActive(true);
            user.setToken(null);
            userRepository.save(user);
        }
        return user;
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void sendPasswordResetEmail(User user) {
        String resetToken = UUID.randomUUID().toString();
        user.setToken(resetToken);
        userRepository.save(user);
    
        String resetUrl = appBaseUrl + "/api/users/reset-password?token=" + resetToken;
        String message = "Please reset your password by clicking the link below:\n" + resetUrl;
        emailService.sendEmail(user.getEmail(), "Password Reset", message);
    }
    
    public User resetPassword(String token, String newPassword) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setToken(null);
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }
}
