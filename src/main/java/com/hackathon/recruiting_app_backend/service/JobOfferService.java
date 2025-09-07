package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.dto.JobOfferUpdateDTO;
import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobOfferService {
    // dependency injection
    private final JobOfferRepository jobOfferRepository;


    // create job offer
    public JobOffer createJobOffer(JobOffer jobOffer, User recruiter, Company company) {
        jobOffer.setUser(recruiter);
        jobOffer.setCompany(company);
        return jobOfferRepository.save(jobOffer);
    }

    // get all job offers
    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    // get job offers by recruiter
    public List<JobOffer> getJobOffersByRecruiter(Long recruiterId) {
        return jobOfferRepository.findByUserId(recruiterId);
    }

    // get job offer by id
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

    // update job offer (using JobOfferUpdateDTO.java)
    public JobOffer updateJobOffer(Long id, JobOfferUpdateDTO jobOfferUpdateDTO, Long userId) {
        JobOffer jobOffer;

        if (userId == null) {
            // ADMIN mode - no ownership check
            jobOffer = jobOfferRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job offer with ID " + id + " not found"));
        } else {
            // RECRUITER mode - ownership check
            jobOffer = jobOfferRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new RuntimeException("Job offer with ID " + id + " NOT FOUND"));
        }

        // Update fields
        jobOffer.setTitle(jobOfferUpdateDTO.title());
        jobOffer.setDescription(jobOfferUpdateDTO.description());
        jobOffer.setLocation(jobOfferUpdateDTO.location());
        jobOffer.setSalary(jobOfferUpdateDTO.salary());
        jobOffer.setEmploymentType(jobOfferUpdateDTO.employmentType());

        return jobOfferRepository.save(jobOffer);
    }

}
