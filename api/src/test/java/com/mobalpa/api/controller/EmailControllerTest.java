package com.mobalpa.api.controller;

import com.mobalpa.api.model.Emailing;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmailSuccess() {
        Emailing emailing = new Emailing();

        ResponseEntity<String> response = emailController.sendEmail(emailing);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent", response.getBody());
    }

    @Test
    void testSendEmailFailure() {
        Emailing emailing = new Emailing();
        doThrow(new IllegalArgumentException("Invalid email")).when(emailService).sendEmail(any(Emailing.class));

        ResponseEntity<String> response = emailController.sendEmail(emailing);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email", response.getBody());
    }

    @Test
    void testSendNewsletterSuccess() {
        Newsletter newsletter = new Newsletter();

        ResponseEntity<String> response = emailController.sendNewsletter(newsletter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Newsletter sent", response.getBody());
    }

    @Test
    void testSendNewsletterFailure() {
        Newsletter newsletter = new Newsletter();
        doThrow(new IllegalArgumentException("Invalid newsletter")).when(emailService).sendNewsletter(any(Newsletter.class));

        ResponseEntity<String> response = emailController.sendNewsletter(newsletter);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid newsletter", response.getBody());
    }

    @Test
    void testDeleteNewsletter() {
        UUID newsletterId = UUID.randomUUID();

        ResponseEntity<String> response = emailController.deleteNewsletter(newsletterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Newsletter deleted", response.getBody());
    }

    @Test
    void testGetEmailStatusSuccess() {
        UUID emailId = UUID.randomUUID();
        Emailing emailing = new Emailing();
        emailing.setStatus(Emailing.EmailingType.SENT);

        when(emailService.getEmailByUuid(any(UUID.class))).thenReturn(Optional.of(emailing));

        ResponseEntity<String> response = emailController.getEmailStatus(emailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SENT", response.getBody());
    }

    @Test
    void testGetEmailStatusNotFound() {
        UUID emailId = UUID.randomUUID();

        when(emailService.getEmailByUuid(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<String> response = emailController.getEmailStatus(emailId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email not found", response.getBody());
    }
}