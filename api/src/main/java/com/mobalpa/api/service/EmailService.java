package com.mobalpa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String templateName, String... replacements) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(getEmailContent(templateName, replacements), true);
        helper.setFrom("mobalpa-new-client@outlook.com");
        
        mailSender.send(message);
    }

    private String getEmailContent(String templateName, String... replacements) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String content = new String(byteArray, StandardCharsets.UTF_8);

        for (int i = 0; i < replacements.length; i += 2) {
            content = content.replace(replacements[i], replacements[i + 1]);
        }

        return content;
    }
}