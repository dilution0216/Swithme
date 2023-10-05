package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;
    private String token;


    public TokenBlacklist() {
    }

    public TokenBlacklist(String token) {
        this.token = token;
    }
}