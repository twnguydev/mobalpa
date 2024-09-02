package com.mobalpa.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String phoneNumber;
    private Boolean terms;
    private Boolean communications;
}
