package com.hackathon.recruiting_app_backend.dto;

public record JobApplicationRequestDTO(
        Long jobOfferId,
        String coverLetter
) {
}
