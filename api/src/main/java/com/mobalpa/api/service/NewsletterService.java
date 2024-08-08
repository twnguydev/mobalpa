package com.mobalpa.api.service;

import org.springframework.stereotype.Service;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;



@Service


public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;
    
    public void saveNewsletter(Newsletter newsletter) {
        newsletterRepository.save(newsletter);
    }

    @Transactional
    public void deleteNewsletterByEmail(String emailUser) {
        newsletterRepository.deleteByEmailUser(emailUser);
    }
}
