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

@RestController
@RequestMapping("/api/emails")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private UserService userService; 

    @PostMapping("/newsletter")
    public ResponseEntity<String> addNewsletter(@RequestBody Newsletter newsletter) {
        User user = userService.getUserByEmail(newsletter.getEmailUser());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        newsletterService.saveNewsletter(newsletter);

        return ResponseEntity.status(HttpStatus.OK).body("Newsletter saved");
    }


    @DeleteMapping("/newsletter")
    public ResponseEntity<String> deleteNewsletter(@RequestBody Newsletter newsletter) {
        newsletterService.deleteNewsletterByEmail(newsletter.getEmailUser());
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter supprimée");
    }
    
}
