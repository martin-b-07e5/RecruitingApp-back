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

    @Override
    public void run(String... args) throws Exception {
        if (userCompanyRepository.count() == 0) {
            // Find recruiter and companies
            User recruiter = userRepository.findByEmail("recruiter@google.com").orElseThrow(() -> new RuntimeException("Recruiter not found"));
            Company google = companyRepository.findByEmail("contact@google.com").orElseThrow(() -> new RuntimeException("Company not found"));
            Company microsoft = companyRepository.findByEmail("support@microsoft.com").orElseThrow(() -> new RuntimeException("Company not found"));

            // Link recruiter to Google
            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter)
                    .company(google)
                    .position("Recruitment Manager")
                    .startDate(LocalDate.now().minusYears(2))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());

            // Link recruiter to Microsoft
            userCompanyRepository.save(UserCompany.builder()
                    .user(recruiter)
                    .company(microsoft)
                    .position("Recruitment Manager")
                    .startDate(LocalDate.now().minusYears(1))
                    .isCurrentlyEmployed(true)
                    .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                    .build());
        }
    }

}
