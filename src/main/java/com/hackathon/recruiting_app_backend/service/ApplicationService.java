package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.model.Application;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    // dependency injection
    private final ApplicationRepository applicationRepository;

    // constructor
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // createApplication
    public Application createApplication(User candidate, JobOffer jobOffer, String coverLetter) {
        // check if application already exists
        if (applicationRepository.existsByJobOfferIdAndCandidateId(jobOffer.getId(), candidate.getId())) {
            throw new RuntimeException("Already applied to this job offer");
        }

        // create application
        Application application = Application.builder()
                .candidate(candidate)
                .jobOffer(jobOffer)
                .coverLetter(coverLetter)
                .status(Application.ApplicationStatus.PENDING)
                .build();

        return applicationRepository.save(application);
    }

}
