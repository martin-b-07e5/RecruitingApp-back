package com.hackathon.recruiting_app_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 254)
    @NotNull(message = "Email must not be null")
    @Size(min = 5, max = 254, message = "Email must be between 5 and 254 characters long")
    @Email(message = "Email must be valid")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password must not be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonIgnore
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotNull(message = "First name must not be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotNull(message = "Last name must not be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
    private String lastName;

    @Column(length = 20)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN,
        CANDIDATE,
        RECRUITER
    }

    @Column
    private String resumeFile;

    @Column
    private String experience;

    // Relationship for both: recruiters and candidates with companies.
    // Represents a user’s association with companies (e.g., recruiters employed by a company or candidates’ work history).
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCompany> companies;

    // Relationship for candidates with applications.
    // Links candidates to their job applications, supporting the application system.
    // cascade = CascadeType.ALL and orphanRemoval = true are used to automatically delete related JobApplication records when a candidate is deleted.
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications;

    // Relationship for recruiters with job offers
    // Links recruiters to the job offers they create, supporting job management.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOffer> jobOffers;  // A recruiter can create many job offers

    // Relationship for users with their skills.
    // Allows candidates to have multiple skills.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

}
