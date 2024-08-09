package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.model.User; 
import com.mobalpa.api.service.NewsletterService;
import com.mobalpa.api.service.UserService; 

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/emails")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private UserService userService; 

    @PostMapping("/newsletter")
public ResponseEntity<Map<String, String>> addNewsletter(@RequestBody Newsletter newsletter) {
    User user = userService.getUserByEmail(newsletter.getEmailUser());
    if (user == null) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    newsletterService.saveNewsletter(newsletter);

    Map<String, String> response = new HashMap<>();
    response.put("message", "Newsletter saved");
    return ResponseEntity.status(HttpStatus.OK).body(response);
}


    @DeleteMapping("/newsletter")
    public ResponseEntity<String> deleteNewsletter(@RequestBody Newsletter newsletter) {
        newsletterService.deleteNewsletterByEmail(newsletter.getEmailUser());
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter supprimée");
    }
    
}
