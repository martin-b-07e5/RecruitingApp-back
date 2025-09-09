package com.hackathon.recruiting_app_backend.security.dto;

import java.util.List;

/* Data is not required in a record */
public record RegisterRequestDTO(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String role,
        // Specific fields for each type of user
        String resumeFile,
        List<String> skills,
        String experience,
        // so that it adds comapany_id when creating a recruiter
        List<Long> companyIds // Changed from companyId to companyIds
) {
}

