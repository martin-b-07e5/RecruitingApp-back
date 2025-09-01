package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.JobOfferRequestDTO;
import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.service.JobOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    // inject dependencies
    private final JobOfferService jobOfferService;
    private final IUserRepository userRepository;
    private final CompanyRepository companyRepository;

    // constructor
    public JobOfferController(JobOfferService jobOfferService, IUserRepository userRepository, CompanyRepository companyRepository) {
        this.jobOfferService = jobOfferService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    // createJobOffer
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

    // getAllJobOffers
    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    // getMyJobOffers
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

    // deleteJobOffer (Solo el reclutador que creó la oferta puede eliminarla)
    // DELETE  http://localhost:8080/api/job-offers/11
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJobOffer(
            @PathVariable
            Long id,
            Authentication authentication) {
        try {
            // 1. Get the authenticated user (recruiter)
            String email = authentication.getName();
            User recruiter = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));

            // 2. First check if job offer EXISTS (without checking recruiter)
            // 404 - When it doesn't exist
            Optional<JobOffer> jobOffer = jobOfferService.getJobOfferById(id);
            if (jobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Job offer not found");
            }

            // 3. Then check if job offer belongs to this recruiter
            // 403 - When you don't have permission
            Optional<JobOffer> recruiterJobOffer = jobOfferService.getJobOfferByIdAndRecruiter(id, recruiter.getId());
            if (recruiterJobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("⛔ You are not authorized to delete this job offer");
            }

            // 4. Delete the job offer
            // 200 - When it is deleted
            jobOfferService.deleteJobOffer(id);
            return ResponseEntity.ok()
//                    .body("✅ Job offer deleted successfully");
//                    .body("✅ Job offer #" + id + " deleted successfully");
                    .body("✅ Job offer '" + jobOffer.get().getTitle() + "' (ID: " + id + ") deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Message from the service.
        }
    }


}
