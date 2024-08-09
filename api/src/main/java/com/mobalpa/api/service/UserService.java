package com.mobalpa.api.service;

import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.RoleRepository;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.util.JwtUtil;
import com.mobalpa.api.model.Role;
import com.mobalpa.api.model.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Lazy;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUuid(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User registerUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);

        Role userRole = roleService.getRoleByName("ROLE_USER");
        user.getRoles().add(userRole);

        userRepository.save(user);
        sendConfirmationEmail(user);
        return user;
    }

    public void sendConfirmationEmail(User user) {
        String confirmationUrl = appBaseUrl + "api/users/confirm?token=" + user.getToken();
        try {
            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Confirmation de l'email",
                    "confirmationEmailTemplate.html",
                    "${user.firstName}", user.getFirstname(),
                    "${confirmationUrl}", confirmationUrl,
                    "${appName}", "Mobalpa");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
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

    @Transactional
    public String generateToken(User user) {
        Optional<User> existingUser = Optional.of(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " not found");
        }

        String token = tokenService.getTokenByEmail(user.getEmail());
        if (token == null) {
            token = jwtUtil.generateToken(user);
            tokenService.saveToken(user.getEmail(), token);
        }
        return token;
    }

    public void sendPasswordResetEmail(User user) {
        String resetToken = UUID.randomUUID().toString();
        user.setToken(resetToken);
        userRepository.save(user);

        String resetUrlWithToken = "http://localhost:4200/reset-password?token=" + resetToken;
        try {
            emailService.sendHtmlEmail(user.getEmail(),
                    "Réinitialisation du mot de passe",
                    "passwordResetTemplate.html",
                    "${resetUrlWithToken}", resetUrlWithToken,
                    "${appName}", "Mobalpa");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
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

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, User user) {
        return userRepository.findById(uuid).map(existingUser -> {
            if (user.getFirstname() != null)
                existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null)
                existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null)
                existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null)
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getPhoneNumber() != null)
                existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getBirthdate() != null)
                existingUser.setBirthdate(user.getBirthdate());
            if (user.getZipcode() != null)
                existingUser.setZipcode(user.getZipcode());
            if (user.getCity() != null)
                existingUser.setCity(user.getCity());
            if (user.getAddress() != null)
                existingUser.setAddress(user.getAddress());

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                Set<Role> roles = user.getRoles().stream().map(role -> roleRepository.findByName(role.getName()))
                        .collect(Collectors.toSet());
                existingUser.setRoles(roles);
            }
    
            existingUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User with id " + uuid + " not found"));
    }         

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }
}
