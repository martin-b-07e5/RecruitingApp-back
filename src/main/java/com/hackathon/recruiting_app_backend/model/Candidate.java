package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "candidates")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("CANDIDATE")
@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor


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

}