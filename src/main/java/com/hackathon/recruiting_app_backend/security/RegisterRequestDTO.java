package com.hackathon.recruiting_app_backend.security;

import java.util.List;

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
        Long companyId
) {
}

