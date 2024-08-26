package com.mobalpa.api.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "visitor")
@Data
public class Visitor implements Person {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @Column(nullable = false)
  private String firstname;

  @Column(nullable = false)
  private String lastname;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = true)
  private String address;

  @Column(nullable = true)
  private String zipcode;

  @Column(nullable = true)
  private String city;

  @Column(nullable = true)
  private boolean active = false;

  @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL)
  private Set<Payment> payments = new HashSet<>();

  @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  @Column(nullable = false, updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt = LocalDateTime.now();
}
