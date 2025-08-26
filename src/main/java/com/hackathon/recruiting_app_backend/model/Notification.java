package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Target user
    private User user; // Each notification belongs to one recipient user

    private String title;
    private String message;
    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
