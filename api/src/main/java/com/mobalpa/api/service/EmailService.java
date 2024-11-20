package com.mobalpa.api.service;

import com.mobalpa.api.model.Emailing;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.repository.EmailingRepository;
import com.mobalpa.api.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailingRepository emailingRepository;

    @Autowired
    private NewsletterRepository newsletterRepository;

    public void sendEmail(Emailing emailing) {

    }

    public void sendNewsletter(Newsletter newsletter) {

    }

    public void deleteNewsletter(UUID newsletterId) {
        newsletterRepository.deleteById(newsletterId);
    }

    public Optional<Emailing> getEmailByUuid(UUID uuid) {
        return emailingRepository.findById(uuid);
    }

    public String getTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    public void sendHtmlEmail(String to, String subject, String templateName, LocalDate date, byte[] attachment, String attachmentName,
            String... replacements) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(getEmailContent(templateName, replacements), true);
        helper.setFrom("mobalpa-new-client@outlook.com");

        if (attachment != null && attachmentName != null && !attachmentName.isEmpty()) {
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
        }

        if (date != null) {
            helper.setSentDate(java.sql.Date.valueOf(date));
        }

        mailSender.send(message);
    }

    private String getEmailContent(String templateName, String... replacements) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
        String content;
        if (resource.exists()) {
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            content = new String(byteArray, StandardCharsets.UTF_8);
        } else {
            content = templateName;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            content = content.replace(replacements[i], replacements[i + 1]);
        }

        return content;
    }
}
