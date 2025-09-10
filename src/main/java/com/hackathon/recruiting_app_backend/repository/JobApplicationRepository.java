package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // applyToJob (msg: You have already applied to this job)
    boolean existsByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId);

    // getCandidateJobApplications
    List<JobApplication> findByCandidateId(Long candidateId);

    // getJobsApplicationsForRecruiters
    List<JobApplication> findByJobOfferUserId(Long userId);

    // To get applications from a job offer
    List<JobApplication> findByJobOfferId(Long jobOfferId);

    // To get applications from a job offer and a candidate
    List<JobApplication> findByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId);

}
