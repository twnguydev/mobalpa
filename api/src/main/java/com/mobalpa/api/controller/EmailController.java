package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.model.Emailing;
import com.mobalpa.api.service.EmailService;
import com.mobalpa.api.service.NewsletterService;

import java.util.Optional;
import java.util.UUID;
import com.mobalpa.api.model.EmailingType;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Emailing emailing) {
        try {
            emailService.sendEmail(emailing);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
   
    @GetMapping("/{uuid}/status")
    public ResponseEntity<EmailingType> getEmailStatus(@PathVariable UUID uuid) {
        try {
            EmailingType status = emailService.getEmailStatus(uuid);
            return ResponseEntity.ok(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
