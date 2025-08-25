package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "phone", length = 20)
    private String phone;

    // one company has 0 or many job offers (inverse relationship in JobOffer must exist)
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<JobOffer> jobOffers = new HashSet<>();

}
