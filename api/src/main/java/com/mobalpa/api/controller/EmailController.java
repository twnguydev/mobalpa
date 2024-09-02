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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Email", description = "APIs for managing emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @Operation(summary = "Send email", description = "Sends an email.", security = @SecurityRequirement(name = "bearerAuth"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
                "to": "recipient@example.com",
                "subject": "Welcome!",
                "body": "Thank you for joining our platform.",
                "status": "SENT"
            }
            """))), responses = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Email sent"
                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid email details provided.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Invalid email details provided."
                    """)))
    })
    public ResponseEntity<String> sendEmail(@RequestBody Emailing emailing) {
        try {
            emailService.sendEmail(emailing);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/newsletter")
    @Operation(summary = "Send newsletter", description = "Sends a newsletter.", security = @SecurityRequirement(name = "bearerAuth"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
                "subject": "Our Latest News!",
                "body": "Here's what's new in our company...",
                "recipients": ["subscriber1@example.com", "subscriber2@example.com"]
            }
            """))), responses = {
            @ApiResponse(responseCode = "200", description = "Newsletter sent successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Newsletter sent"
                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid newsletter details provided.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Invalid newsletter details provided."
                    """)))
    })
    public ResponseEntity<String> sendNewsletter(@RequestBody Newsletter newsletter) {
        try {
            emailService.sendNewsletter(newsletter);
            return ResponseEntity.status(HttpStatus.OK).body("Newsletter sent");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/newsletter")
    @Operation(summary = "Delete newsletter", description = "Deletes a newsletter.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "newsletterId", description = "UUID of the newsletter to be deleted", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"), responses = {
            @ApiResponse(responseCode = "200", description = "Newsletter deleted successfully.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "Newsletter deleted"
                    """))),
            @ApiResponse(responseCode = "404", description = "Newsletter not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "error": "Not Found",
                        "message": "Newsletter with the specified UUID does not exist."
                    }
                    """)))
    })
    public ResponseEntity<String> deleteNewsletter(@RequestParam UUID newsletterId) {
        emailService.deleteNewsletter(newsletterId);
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter deleted");
    }

    @GetMapping("/{uuid}/status")
    @Operation(summary = "Get email status", description = "Fetches the status of an email.", security = @SecurityRequirement(name = "bearerAuth"), parameters = @Parameter(name = "uuid", description = "UUID of the email whose status is to be fetched", required = true, example = "b9f1e7c1-1212-4d1e-897f-4b5d4e8f223d"), responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved email status.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    "SENT"
                    """))),
            @ApiResponse(responseCode = "404", description = "Email not found.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "error": "Not Found",
                        "message": "Email with the specified UUID does not exist."
                    }
                    """)))
    })
    public ResponseEntity<String> getEmailStatus(@PathVariable UUID uuid) {
        Optional<Emailing> emailing = emailService.getEmailByUuid(uuid);
        return emailing
                .map(value -> ResponseEntity.ok(value.getStatus().name()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"));
    }
}
