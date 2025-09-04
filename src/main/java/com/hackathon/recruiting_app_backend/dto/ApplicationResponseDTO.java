package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.Application;
import com.hackathon.recruiting_app_backend.model.Skill;

import java.time.LocalDateTime;
import java.util.List;

// Solve infinite JSON recursion in application endpoints:
public record ApplicationResponseDTO(
        Long id,
        String coverLetter,
        LocalDateTime appliedAt,
        Application.ApplicationStatus status,
        Long jobOfferId,
        String jobOfferTitle,
        Long candidateId,
        String candidateFirstName,
        String candidateLastName,
        String candidatePhone,
        String candidateEmail,
        String candidateResumeFile,
        List<String> candidateSkills,
        String candidateExperience
) {
    public static ApplicationResponseDTO fromEntity(Application application) {
        return new ApplicationResponseDTO(
                application.getId(),
                application.getCoverLetter(),
                application.getAppliedAt(),
                application.getStatus(),
                application.getJobOffer().getId(),
                application.getJobOffer().getTitle(),
                application.getCandidate().getId(),
                application.getCandidate().getFirstName(),
                application.getCandidate().getLastName(),
                application.getCandidate().getPhone(),
                application.getCandidate().getEmail(),
                application.getCandidate().getResumeFile(),
                application.getCandidate().getSkills().stream().map(Skill::getName).toList(),
                application.getCandidate().getExperience()
        );
    }
}
