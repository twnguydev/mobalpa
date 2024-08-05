package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginResponse;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("uniqueemailused_" + System.currentTimeMillis() + "@gmail.com");
        user.setPassword("123456");
        user.setActive(true);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<String> response = authController.registerUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful. Please check your email for confirmation.", response.getBody());
    }

    @Test
    public void testRegisterUser_EmailAlreadyUsed() {
        User user = new User();
        user.setEmail("test_email_" + System.currentTimeMillis() + "@gmail.com");
        user.setPassword("123456");

        doThrow(new RuntimeException("Email is already in use")).when(userService).registerUser(any(User.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.registerUser(user);
        });

        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    public void testLoginUser_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nathanmatounga@gmail.com");
        loginDTO.setPassword("123456");

        User user = new User();
        user.setEmail("nathanmatounga@gmail.com");
        user.setActive(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.generateToken(user)).thenReturn("token");

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponse expectedResponse = new LoginResponse(user, "token");
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("invalid@example.com");
        loginDTO.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    public void testLoginUser_AccountNotActive() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("inactive@example.com");
        loginDTO.setPassword("123456");

        User user = new User();
        user.setEmail("inactive@example.com");
        user.setActive(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Account is not active or does not exist", response.getBody());
    }

    @Test
    public void testLoginUser_UserNotActive() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nathanmatounga@gmail.com");
        loginDTO.setPassword("123456");

        User user = new User();
        user.setEmail("nathanmatounga@gmail.com");
        user.setActive(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        ResponseEntity<?> response = authController.loginUser(loginDTO);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Account is not active or does not exist", response.getBody());
    }

    @Test
    public void testLoginUser_AuthenticationFailed() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("fail@example.com");
        loginDTO.setPassword("123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {});

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed", response.getBody());
    }

    @Test
    public void testLoginUser_InternalServerError() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("error@example.com");
        loginDTO.setPassword("123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = authController.loginUser(loginDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while logging in", response.getBody());
    }

    @Test
    public void testForgotPassword_UserExists() {
        String email = "testuser@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setActive(true);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        when(userService.getUserByEmail(email)).thenReturn(user);

        ResponseEntity<String> response = authController.forgotPassword(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset email sent.", response.getBody());
    }

    @Test
    public void testForgotPassword_UserNotFound() {
        String email = "nonexistentuser@gmail.com";

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        when(userService.getUserByEmail(email)).thenReturn(null);

        ResponseEntity<String> response = authController.forgotPassword(body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found or inactive", response.getBody());
    }

    @Test
    public void testResetPassword_Success() {
        String token = "validToken";
        String newPassword = "newPassword123";
        User user = new User();
        user.setEmail("testuser@gmail.com");

        Map<String, String> body = new HashMap<>();
        body.put("newPassword", newPassword);

        when(userService.resetPassword(token, newPassword)).thenReturn(user);

        ResponseEntity<String> response = authController.resetPassword(token, body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful.", response.getBody());
    }

    @Test
    public void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        String newPassword = "newPassword123";

        Map<String, String> body = new HashMap<>();
        body.put("newPassword", newPassword);

        when(userService.resetPassword(token, newPassword)).thenReturn(null);

        ResponseEntity<String> response = authController.resetPassword(token, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token or user not found.", response.getBody());
    }
}
