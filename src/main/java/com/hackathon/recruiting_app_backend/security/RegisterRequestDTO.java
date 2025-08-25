package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    //
    // Specific fields for each type of user
    private String resumeFile;
    private String skills;
    private String experience;
    // so that it adds comapany_id when creating a recruiter
    private Long companyId;
}