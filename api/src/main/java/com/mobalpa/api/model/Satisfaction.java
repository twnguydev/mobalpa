package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Satisfaction")
@Data
@NoArgsConstructor
public class Satisfaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = true)
    private User userUuid;

    @Column(name = "target_uuid")
    private UUID targetUuid;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

}