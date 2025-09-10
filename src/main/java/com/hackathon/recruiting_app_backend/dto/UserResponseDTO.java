package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.*;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        User.Role role,
        String resumeFile,
        String experience,
        List<String> skills,
        List<Long> companyIds, // Only company id, not the entire Company object
        List<String> companyNames, // Only company name, not the entire Company object
        List<Long> jobApplications,
        List<Long> jobOffers // Job offer IDs for recruiters
) {
    /**
     * Factory method to convert User entity to Response DTO
     * Avoids circular references and exposes only necessary data
     */
    public static UserResponseDTO fromEntityUser(User user) {
        List<Long> companyIds = null;
        List<String> companyNames = null;
        List<Long> jobApplications = null;
        List<Long> jobOffers = null;

        if (user.getRole().equals(User.Role.RECRUITER)) {
            companyIds = user.getCompanies().stream()
                    .map(UserCompany -> UserCompany.getCompany().getId())
                    .toList();
            companyNames = user.getCompanies().stream().map(UserCompany -> UserCompany.getCompany().getName()).toList();
            jobOffers = user.getJobOffers().stream()
                    .map(JobOffer::getId)
                    .toList();
        } else if (user.getRole().equals(User.Role.CANDIDATE)) {
            jobApplications = user.getJobApplications().stream()
                    .map(JobApplication::getId)
                    .toList();
        }
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRole(),
                user.getResumeFile(),
                user.getExperience(),
                user.getSkills().stream().map(Skill::getName).toList(),
                companyIds,
                companyNames,
                jobApplications,
                jobOffers
        );
    }
}
