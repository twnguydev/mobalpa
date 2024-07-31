package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;  
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

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
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.generateToken(user)).thenReturn("token");

        ResponseEntity<String> response = authController.loginUser(loginDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody());
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nathanmatounga@gmail.com");
        loginDTO.setPassword("wrongpassword");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<String> response = authController.loginUser(loginDTO);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
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

        ResponseEntity<String> response = authController.loginUser(loginDTO);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Account is not active or does not exist", response.getBody());
    }

    @Test
    public void testLoginUser_AuthenticationException() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nonexistentuser@gmail.com"); 
        loginDTO.setPassword("123456");

        doThrow(new AuthenticationException("Authentication failed") {
        }).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<String> response = authController.loginUser(loginDTO);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed", response.getBody());
    }
}
