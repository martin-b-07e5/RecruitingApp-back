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
    // dependency injection
    private final JobOfferRepository jobOfferRepository;

    // constructor
    public JobOfferService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    // create job offer
    public JobOffer createJobOffer(JobOffer jobOffer, User recruiter, Company company) {
        jobOffer.setUser(recruiter);
        jobOffer.setCompany(company);
        return jobOfferRepository.save(jobOffer);
    }

    // get job offers by recruiter
    public List<JobOffer> getJobOffersByRecruiter(Long recruiterId) {
        return jobOfferRepository.findByUserId(recruiterId);
    }

    // get all job offers
    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    // --------------------
    // getJobOfferById
    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }

    // getJobOfferByIdAndRecruiter
    public Optional<JobOffer> getJobOfferByIdAndRecruiter(Long id, Long recruiterId) {
        return jobOfferRepository.findByIdAndUserId(id, recruiterId);
    }

    // delete job offer
    public void deleteJobOffer(Long id) {
        if (!jobOfferRepository.existsById(id)) {
            throw new RuntimeException("Job offer with ID " + id + " not found");
        }
        jobOfferRepository.deleteById(id);
    }
    // --------------------


}
