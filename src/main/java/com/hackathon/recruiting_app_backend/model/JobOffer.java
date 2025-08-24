package com.hackathon.recruiting_app_backend.model;

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

    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum EmploymentType {
        FULL_TIME,
        PART_TIME,
        FREELANCE,
        INTERNSHIP
    }

    // Inverse relationship in JobOffer with Company
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // Inverse relationship in JobOffer with Recruiter
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    // one jobOffer has N applications
    @OneToMany(mappedBy = "jobOffer")
    private Set<Application> applications;

//    // one jobOffer has one company
//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;

//    // one jobOffer has one recruiter
//    @ManyToOne
//    @JoinColumn(name = "recruiter_id", nullable = false)
//    private Recruiter recruiter;

//    // one jobOffer has many candidates
//    @OneToMany(mappedBy = "jobOffer")
//    private Set<Candidate> candidates;


}
