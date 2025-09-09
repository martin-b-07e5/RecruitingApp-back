package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserCompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Order(3) // Run after AdminDataLoader and CompanyDataLoader
public class UserCompanyDataLoader implements CommandLineRunner {
    private final UserCompanyRepository userCompanyRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    /*
     * 1. Google
     * 2. Microsoft
     * 3. Apple
     * 4. Amazon
     * 5. Meta
     * */

    @Override
    public void run(String... args) throws Exception {
        if (userCompanyRepository.count() == 0) {
            // Find recruiters
            User recruiter = userRepository.findByEmail("recruiter@google.com").orElseThrow(() -> new RuntimeException("Recruiter not found"));
            User recruiter1 = userRepository.findByEmail("recruiter1@example.com").orElseThrow(() -> new RuntimeException("Recruiter not found"));

            // Find companies
            Company google = companyRepository.findByEmail("contact@google.com").orElseThrow(() -> new RuntimeException("Company not found"));
            Company microsoft = companyRepository.findByEmail("support@microsoft.com").orElseThrow(() -> new RuntimeException("Company not found"));
            Company apple = companyRepository.findByEmail("info@apple.com").orElseThrow(() -> new RuntimeException("Company not found"));
            Company amazon = companyRepository.findByEmail("contact@amazon.com").orElseThrow(() -> new RuntimeException("Company not found"));
            Company meta = companyRepository.findByEmail("info@meta.com").orElseThrow(() -> new RuntimeException("Company not found"));

            // Link recruiter (recruiter@google.com) to Google and Microsoft
            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter)
                    .company(google)
                    .position("Recruitment Manager")
                    .startDate(LocalDate.now().minusYears(2))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());

            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter)
                    .company(microsoft)
                    .position("Recruitment Manager")
                    .startDate(LocalDate.now().minusYears(1))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());

            // Link recruiter1 (recruiter1@google.com) to apple, amazon and meta
            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter1)
                    .company(apple)
                    .position("Recruiter")
                    .startDate(LocalDate.now().minusYears(1))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());

            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter1)
                    .company(amazon)
                    .position("Recruiter")
                    .startDate(LocalDate.now().minusYears(1))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());

            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter1)
                    .company(meta)
                    .position("Recruiter")
                    .startDate(LocalDate.now().minusYears(1))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());
        }
    }

}
