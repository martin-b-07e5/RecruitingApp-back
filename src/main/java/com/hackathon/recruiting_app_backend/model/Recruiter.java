package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruiters")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("RECRUITER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // <- Generates a constructor with all fields (including the parent's)
@SuperBuilder // <- Generates the builder pattern
public class Recruiter extends User {

    // One recruiter creates 0 or many jobOffers
    @OneToMany(mappedBy = "recruiter")
    private List<JobOffer> jobOffers = new ArrayList<>();

    // many jobOffers belong to one company
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
