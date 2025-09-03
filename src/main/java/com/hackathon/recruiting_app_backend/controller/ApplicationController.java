package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.ApplicationRequestDTO;
import com.hackathon.recruiting_app_backend.model.Application;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    // inject dependencies
    private final ApplicationService applicationService;
    private final UserRepository userRepository;
    private final JobOfferRepository jobOfferRepository;

    // constructor
    public ApplicationController(ApplicationService applicationService, UserRepository userRepository, JobOfferRepository jobOfferRepository) {
        this.applicationService = applicationService;
        this.userRepository = userRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    // create application (Candidates can apply to job offers)
    @PostMapping("/create")
    @PreAuthorize("hasRole('CANDIDATE')")
    // POST http://localhost:8080/api/applications/create
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequestDTO requestDTO, Authentication authentication) {
        try {
            // 1. Get the authenticated user (recruiter)
            String email = authentication.getName();
            User candidate = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // 2. Get the job offer
            JobOffer jobOffer = jobOfferRepository.findById(requestDTO.jobOfferId())
                    .orElseThrow(() -> new RuntimeException("Job offer not found"));

            // 3. Create the application
            Application application = applicationService.createApplication(
                    candidate, jobOffer, requestDTO.coverLetter());

            // 4. Return the application
            return ResponseEntity.status(HttpStatus.CREATED).body(application);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

}
