package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.JobOffer;

import java.time.LocalDateTime;

public record JobOfferResponseDTO(
        Long id,
        String title,
        String description,
        String location,
        String salary,
        JobOffer.EmploymentType employmentType,
        LocalDateTime createdAt,
        String companyName, // Only company name, not the entire Company object
        Long companyId,
        Long recruiterId,
        String recruiterFirstName,
        String recruiterLastName,
        String recruiterEmail) {
    /**
     * Factory method to convert JobOffer entity to Response DTO
     * Avoids circular references and exposes only necessary data
     */
    public static JobOfferResponseDTO fromEntity(JobOffer jobOffer) {
        return new JobOfferResponseDTO(
                jobOffer.getId(),
                jobOffer.getTitle(),
                jobOffer.getDescription(),
                jobOffer.getLocation(),
                jobOffer.getSalary(),
                jobOffer.getEmploymentType(),
                jobOffer.getCreatedAt(),
                jobOffer.getCompany().getName(),
                jobOffer.getCompany().getId(),
                jobOffer.getUser().getId(),
                jobOffer.getUser().getFirstName(),
                jobOffer.getUser().getLastName(),
                jobOffer.getUser().getEmail()
        );
    }
}
