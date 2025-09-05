package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.repository.JobOfferRepository;
import com.hackathon.recruiting_app_backend.repository.UserCompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(4) // Run after UserCompanyDataLoader
public class JobDataLoader implements CommandLineRunner {

    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (jobOfferRepository.count() == 0) {
            // Find recruiter
            User recruiter = (userRepository.findByEmail("recruiter@google.com")
                    .orElseThrow(() -> new RuntimeException("Recruiter not found ❗❗❗⚠⚠⚠")));

            // Find companies
            Company google = (companyRepository.findByEmail("contact@google.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));
            Company microsoft = (companyRepository.findByEmail("support@microsoft.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));

            // Validate recruiter is associated with companies
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter.getId(), google.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter is not associated with Google ❗❗❗⚠⚠⚠");
            }
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter.getId(), microsoft.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter is not associated with Microsoft ❗❗❗⚠⚠⚠");
            }

            // Create test job offers
            jobOfferRepository.save(JobOffer.builder()
                    .title("Junior Java Developer")
                    .description("Looking for junior Java developer with Spring Boot knowledge")
                    .location("Remote")
                    .salary("$60,000 - $90,000")
                    .employmentType(JobOffer.EmploymentType.FULL_TIME)
                    .user(recruiter)
                    .company(google)
                    .build());
            jobOfferRepository.save(JobOffer.builder().title("Senior Java Developer").description("Looking for senior Java developer with Spring Boot knowledge.").location("Mountain View, CA").salary("$100,000 - $150,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(microsoft).build());
        }
    }

}
