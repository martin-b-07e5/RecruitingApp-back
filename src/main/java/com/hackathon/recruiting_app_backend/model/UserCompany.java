package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_company")
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

    private String position;  // Recruiter Manage | Software Developer | Full Stack Developer | etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrentlyEmployed;

    @Enumerated(EnumType.STRING)
    private EmploymentRelationshipType relationshipType; // RECRUITER, EMPLOYEE, FORMER_EMPLOYEE

    public enum EmploymentRelationshipType {
        RECRUITER,
        CURRENT_EMPLOYEE,
        FORMER_EMPLOYEE   // ex-employee
    }

}
