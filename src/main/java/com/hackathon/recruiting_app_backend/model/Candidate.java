package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidates")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("CANDIDATE")
@Getter
@Setter
@NoArgsConstructor
public class Candidate extends User {

    @Column(length = 20)
    private String phone;

    @Column(name = "resume_file", length = 254)
    private String resumeFile;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String experience;

    // one candidate has 0 or many applications
    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Application> applications = new HashSet<>();

    // Candidate Constructor
    public Candidate(String email, String password, String firstName, String lastName,
                     String phone, String resumeFile, String skills, String experience) {
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.phone = phone;
        this.resumeFile = resumeFile;
        this.skills = skills;
        this.experience = experience;
    }

}