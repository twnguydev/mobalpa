package com.mobalpa.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "newsletter")
@Data
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "email_user", nullable = false)
    private String emailUser;

}
