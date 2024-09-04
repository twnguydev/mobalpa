package com.mobalpa.api.controller;

import com.mobalpa.api.dto.LoginDTO;
import com.mobalpa.api.dto.LoginRequestDTO;
import com.mobalpa.api.dto.RegisterRequestDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/users")
@Tag(name = "User authentication", description = "APIs for managing user authentication")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Registers a new user.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    +
                    "  \"uuid\": \"4c3b3db8-dcbb-4cb6-aef6-0b36b9851b44\",\n" +
                    "  \"firstname\": \"Tanguy\",\n" +
                    "  \"lastname\": \"Gibrat\",\n" +
                    "  \"email\": \"haha@outlook.fr\",\n" +
                    "  \"phoneNumber\": \"0123456789\",\n" +
                    "  \"birthdate\": \"2022-08-21\",\n" +
                    "  \"roles\": [{\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"ROLE_USER\"\n" +
                    "  }],\n" +
                    "  \"active\": false,\n" +
                    "  \"createdAt\": \"2024-09-01T12:18:20\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid user data provided\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerUser) {
        try {
            if (!registerUser.getTerms()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors", List.of("Vous devez accepter les conditions d'utilisation.")
                ));
            }
            if (registerUser.getPassword() == null || !registerUser.getPassword().equals(registerUser.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "errors", List.of("Les mots de passe ne correspondent pas.")
                ));
            }
            
            User registeredUser = userService.registerUser(registerUser);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie");
            response.put("userUuid", registeredUser.getUuid());  
    
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "errors", List.of(e.getMessage())
            ));
        }
    }
    

    @GetMapping("/confirm")
    @Operation(summary = "Confirm user", description = "Confirms a user account.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account confirmed", content = @Content(mediaType = "text/html", examples = @ExampleObject(value = "<html><body><h1>Account Confirmed</h1></body></html>"))),
            @ApiResponse(responseCode = "400", description = "Invalid confirmation token", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid confirmation token\""))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Error generating HTML content\"")))
    })
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        User user = userService.confirmUser(token);

        try {
            String templatePath;
            if (user != null) {
                templatePath = "src/main/resources/templates/successTemplate.html";
            } else {
                templatePath = "src/main/resources/templates/errorTemplate.html";
            }

            String htmlContent = Files.readString(Paths.get(templatePath));
            return ResponseEntity.ok(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération du contenu HTML.");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Logs in a user.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    +
                    "  \"user\": {\n" +
                    "    \"uuid\": \"4c3b3db8-dcbb-4cb6-aef6-0b36b9851b44\",\n" +
                    "    \"firstname\": \"Tanguy\",\n" +
                    "    \"lastname\": \"Gibrat\",\n" +
                    "    \"email\": \"haha@outlook.fr\"\n" +
                    "  },\n" +
                    "  \"token\": \"jwt-token\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "User account not confirmed", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"User account not confirmed\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = userService.getUserByEmail(loginDTO.getEmail());
        if (user.getToken() != null) {
            return ResponseEntity.badRequest().body("User account not confirmed.");
        }
        String token = userService.generateToken(user);
        return ResponseEntity.ok(new LoginRequestDTO(user, token));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Sends a password reset email.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset email sent", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Password reset email sent.\""))),
            @ApiResponse(responseCode = "400", description = "User not found or inactive", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"User not found or inactive\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User user = userService.getUserByEmail(email);
        if (user == null || !user.isActive()) {
            return ResponseEntity.badRequest().body("User not found or inactive");
        }
        userService.sendPasswordResetEmail(user);
        return ResponseEntity.ok("Password reset email sent.");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets a user's password.", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Password reset successful.\""))),
            @ApiResponse(responseCode = "400", description = "Invalid token or user not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Invalid token or user not found\""))),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "\"Unauthorized\"")))
    })
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        User user = userService.resetPassword(token, newPassword);
        if (user != null) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token or user not found.");
        }
    }
}
