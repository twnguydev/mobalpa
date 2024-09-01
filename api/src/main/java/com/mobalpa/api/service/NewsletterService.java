package com.mobalpa.api.service;

import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    public void saveNewsletter(Newsletter newsletter) {
        newsletterRepository.save(newsletter);
    }

    @Transactional
    public void deleteNewsletterByUuid(UUID uuid) {
        newsletterRepository.deleteById(uuid);
    }
}
