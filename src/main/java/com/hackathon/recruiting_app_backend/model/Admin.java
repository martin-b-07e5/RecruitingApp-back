package com.hackathon.recruiting_app_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "admins")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Admin extends User {
    // Candidate Constructor
//    public Admin(String email, String password, String firstName, String lastName) {
//        super(email, password, firstName, lastName); // ✅ calls User constructor

    }
    

