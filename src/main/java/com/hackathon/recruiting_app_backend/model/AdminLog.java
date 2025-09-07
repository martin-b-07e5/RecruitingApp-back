package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Admin who performed the action
    private User admin;  // Each admin log entry is associated with one admin user who perf

    private String action; // CREATE_USER, DELETE_JOB_OFFER, etc.
    private String targetType; // USER, COMPANY, JOB_OFFER
    private Long targetId; // ID del recurso afectado

    @CreationTimestamp
    private LocalDateTime createdAt;
}
