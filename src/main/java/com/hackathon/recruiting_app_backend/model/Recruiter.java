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
    private Company company;

    //    public Recruiter(Long id, String email, String password, String firstName, String lastName, LocalDateTime createdAt, LocalDateTime updatedAt, Role role, Company company) {
//        super(id, email, password, firstName, lastName, createdAt, updatedAt, role);
//        this.company = company;
//    }
// Constructor con company
    public Recruiter(String email, String password, String firstName, String lastName,
                     Role role, Company company) {
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRole(role);
        this.company = company;
    }
}
