package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.JobOfferRequestDTO;
import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import com.hackathon.recruiting_app_backend.repository.ICompanyRepository;
import com.hackathon.recruiting_app_backend.service.JobOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    // inject dependencies
    private final JobOfferService jobOfferService;
    private final IUserRepository userRepository;
    private final ICompanyRepository companyRepository;

    // constructor
    public JobOfferController(JobOfferService jobOfferService, IUserRepository userRepository, ICompanyRepository companyRepository) {
        this.jobOfferService = jobOfferService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobOffer> createJobOffer(
            @RequestBody
            JobOfferRequestDTO jobOfferRequestDTO,
            Authentication authentication) {

        // 1. Get the authenticated user (recruiter)
        String email = authentication.getName();
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        // 2. Validate that the recruiter has permission for this company
        Company company = companyRepository.findById(jobOfferRequestDTO.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // 3. Verify that the recruiter belongs to the company
        // 3. ⚠️ TEMPORARY: Skip verification until UserCompany is implemented
//        if (!recruiterService.isRecruiterInCompany(recruiter.getId(), company.getId())) {
//            throw new RuntimeException("Recruiter not authorized for this company");
//        }

        // 4. Create JobOffer from DTO
        JobOffer jobOffer = JobOffer.builder()
                .title(jobOfferRequestDTO.title())
                .description(jobOfferRequestDTO.description())
                .location(jobOfferRequestDTO.location())
                .salary(jobOfferRequestDTO.salary())
                .employmentType(jobOfferRequestDTO.employmentType())
                .build();

        // 5. Save
        JobOffer savedOffer = jobOfferService.createJobOffer(jobOffer, recruiter, company);
        return ResponseEntity.ok(savedOffer);
    }

    @GetMapping("/my-job-offers")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobOffer>> getMyJobOffers(Authentication authentication) {
        // Get recruiter ID from authentication
        String email = authentication.getName();
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        List<JobOffer> offers = jobOfferService.getJobOffersByRecruiter(recruiter.getId());
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

}
