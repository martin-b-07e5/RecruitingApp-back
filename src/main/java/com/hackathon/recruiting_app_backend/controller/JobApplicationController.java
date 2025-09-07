package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.JobApplicationRequestDTO;
import com.hackathon.recruiting_app_backend.dto.JobApplicationResponseDTO;
import com.hackathon.recruiting_app_backend.model.JobApplication;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    // inject dependencies
    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    // create application (Candidates can apply to job offers)
    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    // POST http://localhost:8080/api/job-applications/apply
    public ResponseEntity<?> createApplication(@RequestBody JobApplicationRequestDTO requestDTO, Authentication authentication) {
        try {
            // 1. Get the authenticated user
            String email = authentication.getName();
            User candidate = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // 2. Create the jobApplication
            JobApplication jobApplication = jobApplicationService.applyToJob(
                    candidate.getId(),
                    requestDTO.jobOfferId(),
                    requestDTO.coverLetter()
            );

            // 3. Return the jobApplication
//            return ResponseEntity.status(HttpStatus.CREATED).body(jobApplication);
            return ResponseEntity.status(HttpStatus.CREATED).body(JobApplicationResponseDTO.fromEntity(jobApplication));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

}
