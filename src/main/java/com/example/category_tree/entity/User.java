package com.example.category_tree.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

}
