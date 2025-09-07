package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.JobApplication;
import com.hackathon.recruiting_app_backend.model.Skill;

import java.time.LocalDateTime;
import java.util.List;

// Solve infinite JSON recursion in application endpoints:
public record JobApplicationResponseDTO(
        // JobApplication
        Long id,
        String coverLetter,
        LocalDateTime appliedAt,
        JobApplication.ApplicationStatus status,
        // JobOffer
        Long jobOfferId,
        String jobOfferTitle,
        // Candidate
        Long candidateId,
        String candidateFirstName,
        String candidateLastName,
        String candidatePhone,
        String candidateEmail,
        String candidateResumeFile,
        List<String> candidateSkills,
        String candidateExperience
) {
    /**
     * Factory method to convert JobApplication entity to JobApplicationResponseDTO
     * Avoids infinite JSON recursion
     * Exposes only necessary data
     */
    public static JobApplicationResponseDTO fromEntity(JobApplication jobApplication) {
        return new JobApplicationResponseDTO(
                // JobApplication
                jobApplication.getId(),
                jobApplication.getCoverLetter(),
                jobApplication.getAppliedAt(),
                jobApplication.getStatus(),
                // JobOffer
                jobApplication.getJobOffer().getId(),
                jobApplication.getJobOffer().getTitle(),
                // Candidate
                jobApplication.getCandidate().getId(),
                jobApplication.getCandidate().getFirstName(),
                jobApplication.getCandidate().getLastName(),
                jobApplication.getCandidate().getPhone(),
                jobApplication.getCandidate().getEmail(),
                jobApplication.getCandidate().getResumeFile(),
                jobApplication.getCandidate().getSkills().stream().map(Skill::getName).toList(),
                jobApplication.getCandidate().getExperience()
        );
    }
}
