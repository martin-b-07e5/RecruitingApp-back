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
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 254)
    @Email(message = "Email must be valid")
    @Size(min = 5, max = 254, message = "Email must be between 5 and 254 characters long")
    @NotNull(message = "Email must not be null")
    private String email;

    @Column(nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotNull(message = "Password must not be null")
    @JsonIgnore
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    @NotNull(message = "First name must not be null")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
    @NotNull(message = "Last name must not be null")
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

    @ElementCollection
    private List<String> skills;

    @Column
    private String experience;

    // Relationship for both: recruiters and candidates with companies
    @OneToMany(mappedBy = "user")
    private List<UserCompany> companies;

    // Relationship for candidates with applications
    @OneToMany(mappedBy = "candidate")
    private List<Application> applications;

    // Relationship for recruiters with job offers
    @OneToMany(mappedBy = "user")
    private List<JobOffer> jobOffers;  // A recruiter can create many job offers

}
