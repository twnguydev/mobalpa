package com.mobalpa.api.service;

import com.mobalpa.api.model.Satisfaction;
import com.mobalpa.api.model.User;
import com.mobalpa.api.repository.SatisfactionRepository;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.dto.SatisfactionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class SatisfactionService {

    @Autowired
    private SatisfactionRepository satisfactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Satisfaction createSatisfaction(SatisfactionRequestDTO satisfactionRequestDTO) {
        if (satisfactionRequestDTO.getUserUuid() == null) {
            throw new IllegalArgumentException("User UUID must not be null");
        }

        Optional<User> user = userRepository.findById(satisfactionRequestDTO.getUserUuid());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (satisfactionRequestDTO.getRating() < 0 || satisfactionRequestDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        if (satisfactionRequestDTO.getComment() == null || satisfactionRequestDTO.getComment().length() < 10 || satisfactionRequestDTO.getComment().length() > 500) {
            throw new IllegalArgumentException("Comment must be between 10 and 500 characters");
        }
        Satisfaction satisfaction = new Satisfaction();
        satisfaction.setUserUuid(user.get());
        satisfaction.setTargetType(satisfactionRequestDTO.getTargetType());
        satisfaction.setRating(satisfactionRequestDTO.getRating());
        satisfaction.setComment(satisfactionRequestDTO.getComment());

        if (!"MAIN".equalsIgnoreCase(satisfactionRequestDTO.getTargetType())) {
            if (satisfactionRequestDTO.getTargetUuid() == null) {
                throw new IllegalArgumentException("Target UUID must not be null for PRODUCT or ORDER type");
            }
            satisfaction.setTargetUuid(satisfactionRequestDTO.getTargetUuid());
        }

        return satisfactionRepository.save(satisfaction);
    }

    public List<Satisfaction> getAllSatisfaction() {
        return satisfactionRepository.findAll();
    }

    public List<Satisfaction> getFirstThreeSatisfactions() {
        return satisfactionRepository.findTop3ByOrderByCreatedAtDesc();
    }
}