package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruiters")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("RECRUITER")
@Getter
@Setter
@NoArgsConstructor
public class Recruiter extends User {

    // One recruiter creates 0 or many jobOffers
    @OneToMany(mappedBy = "recruiter")
    private List<JobOffer> jobOffers = new ArrayList<>();

    // many jobOffers belong to one company
    @ManyToOne
    @JoinColumn(name = "company_id")
    private String company;

    //    public Recruiter(Long id, String email, String password, String firstName, String lastName, LocalDateTime createdAt, LocalDateTime updatedAt, Role role, Company company) {
//        super(id, email, password, firstName, lastName, createdAt, updatedAt, role);
//        this.company = company;
//    }
// Constructor con company
//    public Recruiter (String email, String password, String firstName, String lastName,
//                     String company) {
//        super(email, password, firstName, lastName); // ✅ calls User constructor
//        this.company = company;
//
//    }



}
