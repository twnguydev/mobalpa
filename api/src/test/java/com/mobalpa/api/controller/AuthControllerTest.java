package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginRequestDTO;
import com.mobalpa.api.dto.RegisterRequestDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testRegisterUser() {
    //     User user = new User();
    //     when(userService.registerUser(any(RegisterRequestDTO.class))).thenReturn(user);

    //     RegisterRequestDTO registerDTO = new RegisterRequestDTO();
    //     registerDTO.setFirstname("John");
    //     registerDTO.setLastname("Doe");
    //     registerDTO.setEmail("john.doe@example.com");
    //     registerDTO.setPassword("password");
    //     registerDTO.setConfirmPassword("password");
    //     registerDTO.setPhoneNumber("1234567890");
    //     registerDTO.setBirthdate(LocalDate.parse("2000-01-01"));
    //     ResponseEntity<?> response = authController.registerUser(registerDTO);

    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());

    //     Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
    //     assertNotNull(responseBody);
    //     assertEquals("Inscription réussie", responseBody.get("message"));
    //     assertNull(responseBody.get("userUuid"));
    // }

    @Test
    void testLoginUserSuccess() {
        User user = new User();
        user.setToken(null);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.generateToken(any(User.class))).thenReturn("token");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("email", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("email");
        loginDTO.setPassword("password");

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new LoginRequestDTO(user, "token"), response.getBody());
    }

    @Test
    void testLoginUserAccountNotConfirmed() {
        User user = new User();
        user.setToken("token");
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("email", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("email");
        loginDTO.setPassword("password");

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User account not confirmed.", response.getBody());
    }

    @Test
    void testForgotPasswordSuccess() {
        User user = new User();
        user.setActive(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        
        ResponseEntity<String> response = authController.forgotPassword(Map.of("email", "email@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset email sent.", response.getBody());
        verify(userService).sendPasswordResetEmail(user);
    }

    @Test
    void testForgotPasswordUserNotFound() {
        when(userService.getUserByEmail(anyString())).thenReturn(null);

        ResponseEntity<String> response = authController.forgotPassword(Map.of("email", "email@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found or inactive", response.getBody());
    }

    @Test
    void testResetPasswordSuccess() {
        User user = new User();
        when(userService.resetPassword(anyString(), anyString())).thenReturn(user);

        ResponseEntity<String> response = authController.resetPassword("valid-token", Map.of("newPassword", "newPassword"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful.", response.getBody());
    }

    @Test
    void testResetPasswordFailure() {
        when(userService.resetPassword(anyString(), anyString())).thenReturn(null);

        ResponseEntity<String> response = authController.resetPassword("invalid-token", Map.of("newPassword", "newPassword"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token or user not found.", response.getBody());
    }
}