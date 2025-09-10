package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.JobApplicationRequestDTO;
import com.hackathon.recruiting_app_backend.dto.JobApplicationResponseDTO;
import com.hackathon.recruiting_app_backend.dto.JobApplicationUpdateDTO;
import com.hackathon.recruiting_app_backend.model.JobApplication;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    // inject dependencies
    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;
    private final JobOfferRepository jobOfferRepository;

    // applyToJob ('CANDIDATE') (Candidates can apply to job offers)
    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    // POST http://localhost:8080/api/job-applications/apply
    public ResponseEntity<?> applyToJob(@RequestBody JobApplicationRequestDTO requestDTO, Authentication authentication) {
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

    // getAllJobApplications ('ADMIN', 'RECRUITER')
    @GetMapping("getAllJobApplications")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    // GET http://localhost:8080/api/job-applications/getAllJobApplications
    public ResponseEntity<?> getAllJobApplications() {
        try {
            List<JobApplicationResponseDTO> applications = jobApplicationService.getAllJobApplications();
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // getJobApplicationById ('ADMIN', 'RECRUITER', 'CANDIDATE')
    @GetMapping("/getJobApplicationById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER', 'CANDIDATE')")
    // GET http://localhost:8080/api/job-applications/getJobApplicationById/3
    public ResponseEntity<?> getJobApplicationById(@PathVariable Long id, Authentication authentication) {
        try {
            JobApplicationResponseDTO application = jobApplicationService.getJobApplicationById(id);
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getRole() == User.Role.RECRUITER) {
                Long jobOfferRecruiterId = jobOfferRepository.findById(application.jobOfferId())
                        .orElseThrow(() -> new RuntimeException("Job offer not found"))
                        .getUser().getId();
                if (!jobOfferRecruiterId.equals(user.getId())) {
                    throw new RuntimeException("Recruiter Not authorized to view this application");
                }
            }

            if (user.getRole() == User.Role.CANDIDATE) {
                if (!application.candidateId().equals(user.getId())) {
                    throw new RuntimeException("Candidate Not authorized to view this application");
                }
            }

            return ResponseEntity.ok(application);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // getCandidateJobApplications ('CANDIDATE')
    @GetMapping("/getCandidateJobApplications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateJobApplications(Authentication authentication) {
        try {
            User candidate = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            List<JobApplicationResponseDTO> applications = jobApplicationService.getCandidateJobApplications(candidate.getId());
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // getJobsApplicationsForRecruiters ('RECRUITER')
    @GetMapping("/getJobsApplicationsForRecruiters")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getJobsApplicationsForRecruiters(Authentication authentication) {
        try {
            User recruiter = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));
            List<JobApplicationResponseDTO> applications = jobApplicationService.getJobsApplicationsForRecruiters(recruiter.getId());
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // updateApplicationStatus ('RECRUITER', 'ADMIN')
    @PutMapping("/updateApplicationStatus/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestBody JobApplicationUpdateDTO updateDTO, Authentication authentication) {
        try {
            // 1. Get the authenticated user
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // 2. Update the application
            JobApplicationResponseDTO application = jobApplicationService.updateApplicationStatus(
                    id,
                    user.getId(),
                    user.getRole(),
                    updateDTO.status()
            );
            // 3. Return the application
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // withdrawApplication ('CANDIDATE', 'RECRUITER', 'ADMIN')
    @DeleteMapping("/withdrawApplication/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<?> withdrawApplication(@PathVariable Long id, Authentication authentication) {
        try {
            // 1. Get the authenticated user
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Withdraw the application
            JobApplicationResponseDTO application = jobApplicationService.withdrawApplication(
                    id,
                    user.getId(),
                    user.getRole()
            );

            // 3. Return the application
            return ResponseEntity.ok(application);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

    // deleteJobApplication ('ADMIN')
    @DeleteMapping("/deleteJobApplication/{id}")
    @PreAuthorize("hasRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<?> deleteJobApplication(@PathVariable Long id, Authentication authentication) {
        try {
            // 1. Get the authenticated user
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 2. Delete the application
            jobApplicationService.deleteJobApplication(
                    id,
                    user.getId(),
                    user.getRole()
            );

            return ResponseEntity.ok("✅ Job application (ID: " + id + ") deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ " + e.getMessage());
        }
    }

}
