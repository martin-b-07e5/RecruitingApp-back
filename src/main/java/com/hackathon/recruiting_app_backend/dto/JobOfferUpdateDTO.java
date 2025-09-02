package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.JobOffer;

public record JobOfferUpdateDTO(
        String title,
        String description,
        String location,
        String salary,
        JobOffer.EmploymentType employmentType
) {
}
