package com.hackathon.recruiting_app_backend.dto;

import java.util.List;

public record UserRequestUpdateDTO(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String resumeFile,
        String experience,
        List<String> skills,
        List<Long> companyIds
) {
}
