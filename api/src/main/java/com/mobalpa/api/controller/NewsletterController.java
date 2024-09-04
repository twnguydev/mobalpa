package com.mobalpa.api.controller;

import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.NewsletterService;
import com.mobalpa.api.service.UserService;
import com.mobalpa.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.mobalpa.api.dto.NewsletterDTO;
import com.mobalpa.api.dto.NewsletterSendDTO;

@RestController
@RequestMapping("/api")
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
}
