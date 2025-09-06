package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//    Optional<JobOffer> findByUserId(Long userId); // To get a job from a recruiter
//    List<JobOffer> findByCompany(Long companyId); // To get jobs from a company
//    Optional<JobOffer> findByCompanyId(Long companyId); // To get a job from a company

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findByUserId(Long userId); // To get jobs from a recruiter

    Optional<JobOffer> findByIdAndUserId(Long id, Long userId); // To get a job from a recruiter

    List<JobOffer> findByCompanyId(Long companyId); // To get jobs from a company
}
