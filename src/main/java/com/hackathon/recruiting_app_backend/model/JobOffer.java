package com.hackathon.recruiting_app_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "salary", nullable = false)
    private String salary;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "employment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    public enum EmploymentType {
        FULL_TIME,
        PART_TIME,
        FREELANCE,
        INTERNSHIP,
        CONTRACTOR,     // For fixed-term contracts
        TEMPORARY,      // Temporary
        REMOTE,         // Remote (if specific)
        HYBRID;         // Hybrid
    }

    // Inverse relationship in JobOffer with Company
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;  // Each job offer belongs to one company

    // one jobOffer has N applications
    @OneToMany(mappedBy = "jobOffer")
    private Set<Application> applications;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Each job offer is created by one recruiter

}
