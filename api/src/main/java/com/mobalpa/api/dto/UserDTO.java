package com.mobalpa.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private LocalDate birthdate;
    private String address;
    private String zipcode;
    private String city;
    private String token;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String role;
    private boolean active;
    private WishlistDTO wishlist;
}
