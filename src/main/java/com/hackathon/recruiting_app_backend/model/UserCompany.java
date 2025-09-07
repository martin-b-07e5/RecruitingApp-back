package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_company")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Many employment records can belong to one user

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;  // Many employment records can belong to one company

    @NotNull
    private String position;  // Recruiter Manage | Software Developer | Full Stack Developer | etc.

    // startDate and endDate: Track employment duration.
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrentlyEmployed;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentRelationshipType relationshipType; // RECRUITER, EMPLOYEE, FORMER_EMPLOYEE

    public enum EmploymentRelationshipType {
        RECRUITER,
        CURRENT_EMPLOYEE,
        FORMER_EMPLOYEE   // ex-employee
    }

}
