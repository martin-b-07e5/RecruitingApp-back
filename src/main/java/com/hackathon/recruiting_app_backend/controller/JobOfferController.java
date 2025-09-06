package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.JobOfferRequestDTO;
import com.hackathon.recruiting_app_backend.dto.JobOfferResponseDTO;
import com.hackathon.recruiting_app_backend.dto.JobOfferUpdateDTO;
import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.JobOffer;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import com.hackathon.recruiting_app_backend.repository.UserCompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.service.JobOfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    // inject dependencies
    private final JobOfferService jobOfferService;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserCompanyRepository userCompanyRepository;

    // constructor
    public JobOfferController(JobOfferService jobOfferService, UserRepository userRepository, CompanyRepository companyRepository, UserCompanyRepository userCompanyRepository) {
        this.jobOfferService = jobOfferService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.userCompanyRepository = userCompanyRepository;
    }

    // createJobOffer (Recruiters can post new job opportunities)
    @PostMapping("/create")
    @PreAuthorize("hasRole('RECRUITER')")
    // POST http://localhost:8080/api/job-offers/create
    public ResponseEntity<?> createJobOffer(@RequestBody JobOfferRequestDTO jobOfferRequestDTO, Authentication authentication) {
        // Change JobOffer to <?> to handle errors

        try {
            // 1. Validate that the company exists
            if (jobOfferRequestDTO.companyId() == null) {
                throw new RuntimeException("Company ID is required");
            }

            // 2. Get the authenticated user (recruiter)
            String email = authentication.getName();
            User recruiter = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));

            // 3. Validate that the recruiter has permission for this company
            Company company = companyRepository.findById(jobOfferRequestDTO.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            // 4. Verify that the recruiter belongs to the company
            if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(
                    recruiter.getId(), company.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                throw new RuntimeException("Recruiter not authorized for this company");
            }

            // 5. Create JobOffer from DTO
            JobOffer jobOffer = JobOffer.builder()
                    .title(jobOfferRequestDTO.title())
                    .description(jobOfferRequestDTO.description())
                    .location(jobOfferRequestDTO.location())
                    .salary(jobOfferRequestDTO.salary())
                    .employmentType(jobOfferRequestDTO.employmentType())
                    .build();

            // 6. Save
            JobOffer savedOffer = jobOfferService.createJobOffer(jobOffer, recruiter, company);
//            return ResponseEntity.ok(savedOffer.getId());
            return ResponseEntity.ok(JobOfferResponseDTO.fromEntity(savedOffer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ " + e.getMessage()); // Error message
        }

    }

    // getAllJobOffers (Anyone can view job offers (no login required))
    @GetMapping("/getAllJobOffers")
    // GET http://localhost:8080/api/job-offers/getAllJobOffers
    public ResponseEntity<?> getAllJobOffers() {
        try {
            List<JobOffer> jobOffers = jobOfferService.getAllJobOffers();

            // convert each JobOffer to JobOfferResponseDTO
            List<JobOfferResponseDTO> jobOfferResponseDTOs = jobOffers.stream()
                    .map(JobOfferResponseDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(jobOfferResponseDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving job offers: " + e.getMessage());
        }
    }

    // getJobOfferById (Anyone can view job offers (no login required))
    @GetMapping("/getJobOfferById/{id}")
    // GET http://localhost:8080/api/job-offers/getJobOfferById/1
    public ResponseEntity<?> getJobOfferById(@PathVariable Long id) {
        try {
            Optional<JobOffer> jobOffer = jobOfferService.getJobOfferById(id);

            if (jobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Job offer not found");
            }

            // Use DTO to avoid circular JSON
            return ResponseEntity.ok(JobOfferResponseDTO.fromEntity(jobOffer.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving job offer: " + e.getMessage());
        }

    }

    // getMyJobOffers (Only the recruiter who created the offer can view it)
    @GetMapping("/getMyJobOffers")
    @PreAuthorize("hasRole('RECRUITER')")
    // GET http://localhost:8080/api/job-offers/getMyJobOffers
    public ResponseEntity<?> getMyJobOffers(Authentication authentication) {
        try {
            // Get recruiter UNIQUE KEY from authentication
            String email = authentication.getName();
            User recruiter = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));

            List<JobOffer> offers = jobOfferService.getJobOffersByRecruiter(recruiter.getId());

            // convert each JobOffer to JobOfferResponseDTO
            List<JobOfferResponseDTO> jobOfferResponseDTOs = offers.stream()
                    .map(JobOfferResponseDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(jobOfferResponseDTOs);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving job offers: " + e.getMessage());
        }
    }

    // updateJobOffer (Recruiter who created it OR Admin can update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
//     PUT http://localhost:8080/api/job-offers/1
    public ResponseEntity<?> updateJobOffer(@PathVariable Long id, @RequestBody JobOfferUpdateDTO jobOfferUpdateDTO, Authentication authentication) {
        try {
            // 1. Get the authenticated user (recruiter)
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User (admin | recruiter) not found"));

            // 2. First check if job offer EXISTS (without checking recruiter). 404 - When it doesn't exist
            Optional<JobOffer> jobOffer = jobOfferService.getJobOfferById(id);
            if (jobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Job offer not found");
            }

            // 3. If user is ADMIN, skip ownership check
            if (user.getRole() == User.Role.ADMIN) {
                JobOffer updatedJobOffer = jobOfferService.updateJobOffer(id, jobOfferUpdateDTO, null);
                return ResponseEntity.ok(JobOfferResponseDTO.fromEntity(updatedJobOffer));
            }

            // 4. If user is RECRUITER, Then check if job offer belongs to this recruiter. 403 - When you don't have permission
            Optional<JobOffer> recruiterJobOffer = jobOfferService.getJobOfferByIdAndRecruiter(id, user.getId());
            if (recruiterJobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("⛔ You are not authorized to update this job offer");
            }

            // 5. Update the job offer. 200 - When it is updated
            JobOffer updatedJobOffer = jobOfferService.updateJobOffer(id, jobOfferUpdateDTO, user.getId());
            return ResponseEntity.ok(JobOfferResponseDTO.fromEntity(updatedJobOffer));

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // deleteJobOffer (Only the recruiter who created the offer can delete it)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER' or hasRole('ADMIN')")
    // DELETE  http://localhost:8080/api/job-offers/11
    public ResponseEntity<?> deleteJobOffer(@PathVariable Long id, Authentication authentication) {
        try {
            // 1. Get the authenticated user (user)
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User (admin | recruiter) not found"));

            // 2. First check if job offer EXISTS (without checking user). 404 - When it doesn't exist
            Optional<JobOffer> jobOffer = jobOfferService.getJobOfferById(id);
            if (jobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Job offer not found");
            }

            // 3. If user is ADMIN, skip ownership check
            if (user.getRole() == User.Role.ADMIN) {
                jobOfferService.deleteJobOffer(id);
                return ResponseEntity.ok()
                        .body("✅ Job offer '" + jobOffer.get().getTitle() + "' (ID: " + id + ") deleted successfully by admin");
            }

            // 4. Then check if job offer belongs to this user. 403 - When you don't have permission
            Optional<JobOffer> recruiterJobOffer = jobOfferService.getJobOfferByIdAndRecruiter(id, user.getId());
            if (recruiterJobOffer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("⛔ You are not authorized to delete this job offer");
            }

            // 5. Delete the job offer. 200 - When it is deleted
            jobOfferService.deleteJobOffer(id);
            return ResponseEntity.ok()
                    .body("✅ Job offer '" + jobOffer.get().getTitle() + "' (ID: " + id + ") deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Message from the service.
        }
    }

}
