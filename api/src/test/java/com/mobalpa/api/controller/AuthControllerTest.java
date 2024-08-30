package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginRequestDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testRegisterUser() {
        User user = new User();
        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = authController.registerUser(user);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testConfirmUserSuccess() throws IOException {
        User user = new User();
        when(userService.confirmUser(anyString())).thenReturn(user);

        String templateContent = "Success";
        Files.writeString(Paths.get("src/main/resources/templates/successTemplate.html"), templateContent);

        ResponseEntity<String> response = authController.confirmUser("valid-token");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(templateContent, response.getBody());
    }

    @Test
    void testConfirmUserFailure() throws IOException {
        when(userService.confirmUser(anyString())).thenReturn(null);

        String templateContent = "Error";
        Files.writeString(Paths.get("src/main/resources/templates/errorTemplate.html"), templateContent);

        ResponseEntity<String> response = authController.confirmUser("invalid-token");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(templateContent, response.getBody());
    }

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