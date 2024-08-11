package com.mobalpa.api.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }
}