package com.mobalpa.api.controller;

import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.NewsletterService;
import com.mobalpa.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private UserService userService;

    @PostMapping("/newsletter")
    public ResponseEntity<Map<String, String>> addNewsletter(@RequestBody Map<String, String> request) {
        String userUuidStr = request.get("user_uuid");
        UUID userUuid;
        try {
            userUuid = UUID.fromString(userUuidStr);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid UUID format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    
        User user = userService.getUserByUuid(userUuid);
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    
        Newsletter newsletter = new Newsletter();
        newsletter.setUser(user);
    
        newsletterService.saveNewsletter(newsletter);
    
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Newsletter saved");
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }
    

    @DeleteMapping("/newsletter/{uuid}")
    public ResponseEntity<String> deleteNewsletter(@PathVariable UUID uuid) {
        newsletterService.deleteNewsletterByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter deleted");
    }
}