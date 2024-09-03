package com.mobalpa.api.controller.admin;

import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.model.User;
import com.mobalpa.api.dto.NewsletterDTO;
import com.mobalpa.api.dto.NewsletterSendDTO;
import com.mobalpa.api.service.NewsletterService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.EmailService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController("NewsletterControllerAdmin")
@RequestMapping("/api/admin")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

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

        if (newsletterService.isUserSubscribed(userUuid)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "User is already subscribed to the newsletter");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        Newsletter newsletter = new Newsletter();
        newsletter.setUser(user);

        newsletterService.saveNewsletter(newsletter);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Newsletter saved");
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @GetMapping("/newsletters")
    public ResponseEntity<List<NewsletterDTO>> getAllNewsletters() {
        List<Newsletter> newsletters = newsletterService.getAllNewsletters();
        List<NewsletterDTO> newsletterDTOs = newsletters.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(newsletterDTOs);
    }

    @GetMapping("/newsletter/is-subscribed/{userUuid}")
    public ResponseEntity<Map<String, Boolean>> isSubscribed(@PathVariable UUID userUuid) {
        boolean isSubscribed = newsletterService.isUserSubscribed(userUuid);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isSubscribed", isSubscribed);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/newsletter/{uuid}")
    public ResponseEntity<String> deleteNewsletter(@PathVariable UUID uuid) {
        newsletterService.deleteNewsletterByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body("Newsletter deleted");
    }

    private NewsletterDTO convertToDTO(Newsletter newsletter) {
        NewsletterDTO dto = new NewsletterDTO();
        dto.setId(newsletter.getUuid());
        dto.setEmail(newsletter.getUser().getEmail());
        return dto;
    }

    @PostMapping("/newsletter/send")
    public ResponseEntity<Map<String, String>> sendNewsletter(@RequestBody NewsletterSendDTO newsletterSendDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Map<String, String> placeholders = new HashMap<>();

            placeholders.put("${subject}", newsletterSendDTO.getSubject());
            placeholders.put("${contentInParagraphStrings}", String.join("\n\n", newsletterSendDTO.getContentInParagraphStrings()));

            if (newsletterSendDTO.getCampaign() != null) {
                placeholders.put("${campaign.name}", newsletterSendDTO.getCampaign().getName());
            } else {
                placeholders.put("${campaign.name}", "");
            }

            for (int i = 0; i < newsletterSendDTO.getProducts().length; i++) {
                placeholders.put("${product" + i + ".name}", newsletterSendDTO.getProducts()[i].getName());
                placeholders.put("${product" + i + ".description}", newsletterSendDTO.getProducts()[i].getDescription());
                placeholders.put("${product" + i + ".price}", String.format("%.2f€", newsletterSendDTO.getProducts()[i].getPrice()));
            }

            int maxProducts = 5;
            for (int i = newsletterSendDTO.getProducts().length; i < maxProducts; i++) {
                placeholders.put("${product" + i + ".name}", "");
                placeholders.put("${product" + i + ".description}", "");
                placeholders.put("${product" + i + ".price}", "");
            }

            String template = emailService.getTemplate("newsletter_template.html");

            String filledTemplate = replacePlaceholders(template, placeholders);

            for (String email : newsletterSendDTO.getEmails()) {
                emailService.sendHtmlEmail(
                    email,
                    newsletterSendDTO.getSubject(),
                    filledTemplate,
                    newsletterSendDTO.getSendDate(),
                    null,
                    null
                );
            }
    
            response.put("message", "Newsletter sent successfully");
            return ResponseEntity.ok(response);
    
        } catch (MessagingException | IOException e) {
            response.put("message", "Failed to send newsletter");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }
}
