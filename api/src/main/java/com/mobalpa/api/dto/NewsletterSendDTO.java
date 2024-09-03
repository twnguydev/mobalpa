package com.mobalpa.api.dto;

import org.springframework.cglib.core.Local;

import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.model.Campaign;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsletterSendDTO {
  private String subject;
  private String[] contentInParagraphStrings;
  private String[] emails;
  private ProductDTO[] products;
  private Campaign campaign;
  private LocalDate sendDate;
}
