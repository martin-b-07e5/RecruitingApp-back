package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    // url to file
    @Column(nullable = false)
    private String resume_file;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String skills;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String experience;

    // --- relations
    //    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
//    private List<Application> applications;

}
