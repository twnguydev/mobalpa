package com.mobalpa.api.service;

import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUuid(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, User user) {
        return userRepository.findById(uuid).map(existingUser -> {
            if (user.getFirstname() != null) existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null) existingUser.setPassword(user.getPassword());
            if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getBirthdate() != null) existingUser.setBirthdate(user.getBirthdate());
            if (user.getToken() != null) existingUser.setToken(user.getToken());
            if (user.getZipcode() != null) existingUser.setZipcode(user.getZipcode());
            if (user.getCity() != null) existingUser.setCity(user.getCity());
            if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
            if (user.getUpdatedAt() != null) existingUser.setUpdatedAt(user.getUpdatedAt());
            if (user.getRoles() != null) existingUser.setRoles(user.getRoles());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User with id " + uuid + " not found"));
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }
}