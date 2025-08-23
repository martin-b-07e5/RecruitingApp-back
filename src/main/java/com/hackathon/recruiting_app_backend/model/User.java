package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder  // ver para qué se utiliza esto
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Role {
        ADMIN,
        CANDIDATE,
        RECRUITER
    }

    // Relations
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Candidate candidate;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Recruiter recruiter;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Notification> notifications;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<AdminLog> adminLogs;



}





