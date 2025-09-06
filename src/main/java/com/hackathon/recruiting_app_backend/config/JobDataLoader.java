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
            User recruiter1 = (userRepository.findByEmail("recruiter1@example.com")
                    .orElseThrow(() -> new RuntimeException("Recruiter not found ❗❗❗⚠⚠⚠")));

            // Find companies
            Company google = (companyRepository.findByEmail("contact@google.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));
            Company microsoft = (companyRepository.findByEmail("support@microsoft.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));
            Company apple = (companyRepository.findByEmail("info@apple.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));
            Company amazon = (companyRepository.findByEmail("contact@amazon.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));
            Company meta = (companyRepository.findByEmail("info@meta.com")
                    .orElseThrow(() -> new RuntimeException("Company not found ❗❗❗⚠⚠⚠")));

            // Validate recruiter is associated with companies
            // recruiter
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter.getId(), google.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter is not associated with Google ❗❗❗⚠⚠⚠");
            }
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter.getId(), microsoft.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter is not associated with Microsoft ❗❗❗⚠⚠⚠");
            }
            // recruiter1
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter1.getId(), apple.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter1 is not associated with Apple ❗❗❗⚠⚠⚠");
            }
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter1.getId(), amazon.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter1 is not associated with Amazon ❗❗❗⚠⚠⚠");
            }
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(recruiter1.getId(), meta.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter1 is not associated with Meta ❗❗❗⚠⚠⚠");
            }

            // Create test job offers
            // Google
            jobOfferRepository.save(JobOffer.builder().title("Junior Java Developer").description("Looking for junior Java developer with Spring Boot knowledge").location("Remote").salary("$60,000 - $90,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(google).build());
            jobOfferRepository.save(JobOffer.builder().title("Frontend Developer").description("Build web apps with Angular").location("Mountain View, CA").salary("$95,000 - $140,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(google).build());
            jobOfferRepository.save(JobOffer.builder().title("UX Designer").description("Design user-friendly Google products").location("San Francisco, CA").salary("$100,000 - $150,000").employmentType(JobOffer.EmploymentType.PART_TIME).user(recruiter).company(google).build());
            jobOfferRepository.save(JobOffer.builder().title("QA Tester").description("Test AI-driven applications").location("Remote").salary("$80,000 - $115,000").employmentType(JobOffer.EmploymentType.FREELANCE).user(recruiter).company(google).build());

            // Microsoft
            jobOfferRepository.save(JobOffer.builder().title("Senior Java Developer").description("Looking for senior Java developer with Spring Boot knowledge").location("Mountain View, CA").salary("$100,000 - $150,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(microsoft).build());
            jobOfferRepository.save(JobOffer.builder().title("Frontend Developer").description("Develop Azure dashboards with React").location("Redmond, WA").salary("$90,000 - $135,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(microsoft).build());
            jobOfferRepository.save(JobOffer.builder().title("UX Designer").description("Design intuitive cloud interfaces").location("Remote").salary("$95,000 - $140,000").employmentType(JobOffer.EmploymentType.FREELANCE).user(recruiter).company(microsoft).build());
            jobOfferRepository.save(JobOffer.builder().title("QA Tester").description("Test Windows applications").location("Boston, MA").salary("$75,000 - $110,000").employmentType(JobOffer.EmploymentType.PART_TIME).user(recruiter).company(microsoft).build());

            // Apple
            jobOfferRepository.save(JobOffer.builder().title("Frontend Developer").description("React and TypeScript expert needed").location("Cupertino, CA").salary("$85,000 - $125,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter1).company(apple).build());
            jobOfferRepository.save(JobOffer.builder().title("UX Designer").description("Skilled UX designer with Figma expertise").location("Remote").salary("$90,000 - $130,000").employmentType(JobOffer.EmploymentType.PART_TIME).user(recruiter1).company(apple).build());
            jobOfferRepository.save(JobOffer.builder().title("QA Tester").description("QA tester with automation skills").location("Austin, TX").salary("$75,000 - $110,000").employmentType(JobOffer.EmploymentType.FREELANCE).user(recruiter1).company(apple).build());

            // Amazon
            jobOfferRepository.save(JobOffer.builder().title("Frontend Developer").description("Build scalable web apps with React").location("Seattle, WA").salary("$90,000 - $140,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(amazon).build());
            jobOfferRepository.save(JobOffer.builder().title("UX Designer").description("Design intuitive interfaces with Figma").location("New York, NY").salary("$95,000 - $135,000").employmentType(JobOffer.EmploymentType.PART_TIME).user(recruiter1).company(amazon).build());
            jobOfferRepository.save(JobOffer.builder().title("QA Tester").description("Automate testing for cloud services").location("Remote").salary("$80,000 - $115,000").employmentType(JobOffer.EmploymentType.FREELANCE).user(recruiter1).company(amazon).build());

            // Meta
            jobOfferRepository.save(JobOffer.builder().title("Frontend Developer").description("Develop VR interfaces with React").location("Menlo Park, CA").salary("$100,000 - $150,000").employmentType(JobOffer.EmploymentType.FULL_TIME).user(recruiter).company(meta).build());
            jobOfferRepository.save(JobOffer.builder().title("UX Designer").description("Create engaging VR/AR experiences").location("Remote").salary("$105,000 - $145,000").employmentType(JobOffer.EmploymentType.FREELANCE).user(recruiter).company(meta).build());
            jobOfferRepository.save(JobOffer.builder().title("QA Tester").description("Test VR/AR applications").location("London, UK").salary("$85,000 - $120,000").employmentType(JobOffer.EmploymentType.PART_TIME).user(recruiter).company(meta).build());
        }
    }

}
