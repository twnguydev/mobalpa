package com.mobalpa.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobalpa.catalogue.repository.SubcategoryRepository;
import com.mobalpa.catalogue.model.Subcategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubcategoryService {
  
  @Autowired
  private SubcategoryRepository subcategoryRepository;

  public List<Subcategory> getAllSubcategories() {
    return subcategoryRepository.findAll();
  }

  public Optional<Subcategory> getSubcategoryById(UUID id) {
    return subcategoryRepository.findById(id);
  }

  public Subcategory createSubcategory(Subcategory subcategory) {
    return subcategoryRepository.save(subcategory);
  }

  public Subcategory updateSubcategory(UUID id, Subcategory subcategory) {
    subcategory.setUuid(id);
    return subcategoryRepository.save(subcategory);
  }

  public void deleteSubcategory(UUID id) {
    subcategoryRepository.deleteById(id);
  }
}
