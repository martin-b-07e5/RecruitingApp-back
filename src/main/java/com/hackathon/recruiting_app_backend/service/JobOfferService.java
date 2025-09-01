package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    public JobOffer createJobOffer(JobOffer jobOffer, User recruiter, Company company) {
        jobOffer.setUser(recruiter);
        jobOffer.setCompany(company);
        return jobOfferRepository.save(jobOffer);
    }

    // multiple job offers by recruiter
    public List<JobOffer> getJobOffersByRecruiter(Long recruiterId) {
        return jobOfferRepository.findByUserId(recruiterId);
    }

    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }
}
