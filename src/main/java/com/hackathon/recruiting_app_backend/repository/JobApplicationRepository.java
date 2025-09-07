package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId); // To check if an application already exists

    List<JobApplication> findByJobOfferId(Long jobOfferId); // To get applications from a job offer

    List<JobApplication> findByCandidateId(Long candidateId); // To get applications from a candidate

    List<JobApplication> findByJobOfferUserId(Long userId); // To get applications from a recruiter

    List<JobApplication> findByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId); // To get applications from a job offer and a candidate

}
