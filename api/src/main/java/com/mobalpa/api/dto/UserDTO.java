package com.mobalpa.api.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserDTO {
    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("birthdate")
    private LocalDate birthdate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipcode")
    private String zipcode;

    @JsonProperty("city")
    private String city;

    @JsonIgnore
    private String password;

    @JsonProperty("token")
    private String token;

    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("updatedAt")
    private LocalDate updatedAt;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("wishlist")
    private WishlistDTO wishlist;
}