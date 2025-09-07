package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.dto.JobApplicationResponseDTO;
import com.hackathon.recruiting_app_backend.model.JobApplication;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.JobApplicationRepository;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    // dependency injection
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;

    // applyToJob
    public JobApplication applyToJob(Long candidateId, Long jobOfferId, String coverLetter) {

        User candidate = userRepository.findById(candidateId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElseThrow(() -> new RuntimeException("Job offer not found"));

        if (!candidate.getRole().equals(User.Role.CANDIDATE)) {
            throw new RuntimeException("Only candidates can apply to jobs");
        }
        if (jobApplicationRepository.existsByJobOfferIdAndCandidateId(jobOfferId, candidateId)) {
            throw new RuntimeException("You have already applied to this job");
        }

        // create application
        JobApplication application = JobApplication.builder()
                .jobOffer(jobOffer)
                .candidate(candidate)
                .coverLetter(coverLetter)
                .status(JobApplication.ApplicationStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();

        return jobApplicationRepository.save(application);
    }

    // Get all job applications (Admin|Recruiter)
    public List<JobApplicationResponseDTO> getAllJobApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // getJobApplicationById (Admin|Recruiter)
    public JobApplicationResponseDTO getJobApplicationById(Long id) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job application not found"));
        return JobApplicationResponseDTO.fromEntity(application);
    }

    // Get candidate's job applications
    public List<JobApplicationResponseDTO> geCandidateJobApplications(Long candidateId) {
        return jobApplicationRepository.findByCandidateId(candidateId).stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Get applications for recruiter's jobs
    public List<JobApplicationResponseDTO> getJobsApplicationsForRecruiters(Long recruiterId) {
        return jobApplicationRepository.findByJobOfferUserId(recruiterId).stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
