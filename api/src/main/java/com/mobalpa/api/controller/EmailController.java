package com.mobalpa.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobalpa.api.model.Emailing;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.service.EmailService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Emailing emailing) {
        try {
            emailService.sendEmail(emailing);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/newsletter")
    public ResponseEntity<String> sendNewsletter(@RequestBody Newsletter newsletter) {
        try {
            emailService.sendNewsletter(newsletter);
            return ResponseEntity.status(HttpStatus.OK).body("Newsletter sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
    @DeleteMapping("/newsletter")
    public ResponseEntity<String> deleteNewsletter(@RequestParam UUID newsletterId) {
        emailService.deleteNewsletter(newsletterId);
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter deleted");
    }

    @GetMapping("/{uuid}/status")
    public ResponseEntity<String> getEmailStatus(@PathVariable UUID uuid) {
        Optional<Emailing> emailing = emailService.getEmailByUuid(uuid);
        return emailing
                .map(value -> ResponseEntity.ok(value.getStatus().name()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"));
    }
}
