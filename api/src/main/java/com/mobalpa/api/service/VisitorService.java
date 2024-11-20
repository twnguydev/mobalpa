package com.mobalpa.api.service;

import com.mobalpa.api.model.Visitor;
import com.mobalpa.api.repository.VisitorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class VisitorService {

  @Autowired
  private VisitorRepository visitorRepository;

  @Value("${app.base.url}")
  private String appBaseUrl;

  public List<Visitor> getAllVisitors() {
    return visitorRepository.findAll();
  }

  public Visitor getUserByEmail(String email) {
    return visitorRepository.findByEmail(email);
  }

  public Visitor getVisitorByUuid(UUID uuid) {
    return visitorRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Visitor not found"));
  }

  public Visitor registerVisitor(Visitor visitor) {
    return visitorRepository.save(visitor);
  }

  public void deleteVisitor(UUID uuid) {
    visitorRepository.deleteById(uuid);
  }
}