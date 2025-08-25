package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // <- Generates a constructor with all fields (including the parent's)
@SuperBuilder  // <- Generates the builder pattern
public class Admin extends User {

    @Column(name = "resume_file", length = 254)
    private String resumeFile;

    @Column(columnDefinition = "TEXT")
    @Size(min = 2, max = 254, message = "Skills must be between 2 and 254 characters long")
    @NotNull(message = "Skills must not be null")
    private String skills;

    @Column(columnDefinition = "TEXT")
    @Size(min = 2, max = 254, message = "Experience must be between 2 and 254 characters long")
    @NotNull(message = "Experience must not be null")
    private String experience;

}
