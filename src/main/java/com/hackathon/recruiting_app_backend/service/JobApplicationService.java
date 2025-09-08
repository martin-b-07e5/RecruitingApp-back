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

    // applyToJob (CANDIDATE)
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

    // getAllJobApplications ('ADMIN', 'RECRUITER')
    public List<JobApplicationResponseDTO> getAllJobApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // getJobApplicationById ('ADMIN', 'RECRUITER', 'CANDIDATE')
    public JobApplicationResponseDTO getJobApplicationById(Long id) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job application not found"));
        return JobApplicationResponseDTO.fromEntity(application);
    }

    // geCandidateJobApplications ('CANDIDATE)
    public List<JobApplicationResponseDTO> geCandidateJobApplications(Long candidateId) {
        return jobApplicationRepository.findByCandidateId(candidateId).stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // getJobsApplicationsForRecruiters ('RECRUITER')
    public List<JobApplicationResponseDTO> getJobsApplicationsForRecruiters(Long recruiterId) {
        return jobApplicationRepository.findByJobOfferUserId(recruiterId).stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // updateApplicationStatus ('RECRUITER', 'ADMIN')
    public JobApplicationResponseDTO updateApplicationStatus(Long id, Long userId, User.Role role, JobApplication.ApplicationStatus status) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        // Recruiter can update his job offer applications
        if (role == User.Role.RECRUITER && !application.getJobOffer().getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the recruiter of this job application");
        }

        // Candidates cannot update application status
        if (role == User.Role.CANDIDATE) {
            throw new RuntimeException("Candidates cannot update application status");
        }

        // Cannot set status to DRAFT or WITHDRAWN
        if (status == JobApplication.ApplicationStatus.DRAFT || status == JobApplication.ApplicationStatus.WITHDRAWN) {
            throw new RuntimeException("Cannot set status to DRAFT or WITHDRAWN");
        }

        application.setStatus(status);
        return JobApplicationResponseDTO.fromEntity(jobApplicationRepository.save(application));
    }

    // withdrawApplication ('CANDIDATE', 'RECRUITER', 'ADMIN')
    public JobApplicationResponseDTO withdrawApplication(Long id, Long userId, User.Role role) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        // Candidate can only withdraw their own application
        if (role == User.Role.CANDIDATE && !application.getCandidate().getId().equals(userId)) {
            throw new RuntimeException("You are not the candidate of this job application");
        }

        // Recruiter can withdraw any application
        if (role == User.Role.RECRUITER && !application.getJobOffer().getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the recruiter of this job application");
        }

        application.setStatus(JobApplication.ApplicationStatus.WITHDRAWN);
        return JobApplicationResponseDTO.fromEntity(jobApplicationRepository.save(application));
    }

    // deleteJobApplication ('ADMIN', 'RECRUITER')
    public void deleteJobApplication(Long id, Long userId, User.Role role) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        if (role == User.Role.CANDIDATE) {
            throw new RuntimeException("Candidates cannot delete job applications");
        }

        if (role == User.Role.RECRUITER && !application.getJobOffer().getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the recruiter of this job application");
        }
        
        jobApplicationRepository.delete(application);
    }

}
