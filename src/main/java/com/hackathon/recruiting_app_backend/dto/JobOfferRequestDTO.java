package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.JobOffer;

// used in POST http://localhost:8080/api/job-offers/create
public record JobOfferRequestDTO(
        String title,
        String description,
        String location,
        String salary,
        JobOffer.EmploymentType employmentType,
        Long companyId
) {
}