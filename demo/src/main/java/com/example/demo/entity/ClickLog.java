package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ClickLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortcode;
    private LocalDateTime timestamp;
    private String referrer;
    private String location;

    // Getters and Setters
}
