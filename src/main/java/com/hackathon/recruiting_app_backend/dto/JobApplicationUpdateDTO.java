package com.hackathon.recruiting_app_backend.dto;

import com.hackathon.recruiting_app_backend.model.JobApplication;

// for status updates.
public record JobApplicationUpdateDTO(
        JobApplication.ApplicationStatus status
) {
}
