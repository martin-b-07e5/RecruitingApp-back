package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Inverse relationship in Application with JobOffer
    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer; // Each application is for one job offer (🎯 For which job)

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private User candidate;  // Each application belongs to one candidate (👤 Who applies)

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @CreationTimestamp
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    public enum ApplicationStatus {
        DRAFT,          // For the recruiter
        PENDING,        // Application submitted
        UNDER_REVIEW,   // Under review
        INTERVIEW,      // Moved on to interview
        ACCEPTED,
        REJECTED,
        WITHDRAWN       // Candidate withdrew application
    }

}

