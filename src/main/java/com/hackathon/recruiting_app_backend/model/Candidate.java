package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidates")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("CANDIDATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // <- Generates a constructor with all fields (including the parent's)
@SuperBuilder  // <- Generates the builder pattern
public class Candidate extends User {

    @Column(name = "resume_file", length = 254)
    private String resumeFile;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String experience;

    // one candidate has 0 or many applications
    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Application> applications = new HashSet<>();

    /* Constructor to create new Candidates
       Not required with:
       @AllArgsConstructor
       @Builder
     */
//    public Candidate(String email, String password, String firstName, String lastName, String phone,
//                     String resumeFile, String skills, String experience) {
//        // Call the parent class's constructor (User), forcing Role.CANDIDATE
//        // Leave id, createdAt, and updatedAt null. JPA/Hibernate will handle them.
//        super(null, email, password, firstName, lastName, null, null, Role.CANDIDATE);
//        this.phone = phone;
//        this.resumeFile = resumeFile;
//        this.skills = skills;
//        this.experience = experience;
//       // Don't initialize 'applications', it is already initialized with new HashSet<>() above.
//    }

}