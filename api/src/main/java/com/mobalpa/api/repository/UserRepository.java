package com.mobalpa.api.repository;

import com.mobalpa.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByToken(String token);
    User findByEmail(String email);
    Optional<User> findByUuid(UUID uuid);
    List<User> findAllByBirthdate(LocalDate birthdate);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders LEFT JOIN FETCH u.tickets WHERE u.uuid = :userUuid")
    Optional<User> findByIdWithDetails(@Param("userUuid") UUID userUuid);
}
