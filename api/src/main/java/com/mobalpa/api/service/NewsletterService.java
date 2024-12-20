package com.mobalpa.api.service;

import com.mobalpa.api.dto.NewsletterSendDTO;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.Campaign;
import com.mobalpa.api.model.Newsletter;
import com.mobalpa.api.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

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

  @Transactional(readOnly = true)
  public List<Newsletter> getAllNewsletters() {
    return newsletterRepository.findAllWithUsers();
  }

  public boolean isUserSubscribed(UUID userUuid) {
    return newsletterRepository.existsByUser_Uuid(userUuid);
  }
}