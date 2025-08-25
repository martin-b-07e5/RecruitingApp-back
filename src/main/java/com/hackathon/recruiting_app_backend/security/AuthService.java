package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.Candidate;
import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.Recruiter;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.CandidateRepository;
import com.hackathon.recruiting_app_backend.repository.ICompanyRepository;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import com.hackathon.recruiting_app_backend.repository.RecruiterRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import static com.hackathon.recruiting_app_backend.model.User.Role.*;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final ICompanyRepository companyRepository;
    //    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(IUserRepository userRepository,
                       CandidateRepository candidateRepository,
                       RecruiterRepository recruiterRepository,
                       ICompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User savedUser;

        switch (request.getRole()) {
            case "CANDIDATE":
                Candidate candidate = new Candidate();
                candidate.setEmail(request.getEmail());
                candidate.setPassword(passwordEncoder.encode(request.getPassword()));
                candidate.setFirstName(request.getFirstName());
                candidate.setLastName(request.getLastName());
                candidate.setPhone(request.getPhone());
                candidate.setRole(User.Role.CANDIDATE);
                candidate.setResumeFile(request.getResumeFile());
                candidate.setSkills(request.getSkills());
                candidate.setExperience(request.getExperience());
                savedUser = candidateRepository.save(candidate);
                break;

            case "RECRUITER":
                Recruiter recruiter = new Recruiter();
                recruiter.setEmail(request.getEmail());
                recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
                recruiter.setFirstName(request.getFirstName());
                recruiter.setLastName(request.getLastName());
                recruiter.setPhone(request.getPhone());
                recruiter.setRole(User.Role.RECRUITER);

                if (request.getCompanyId() != null) {
                    Company company = companyRepository.findById(request.getCompanyId())
                            .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.getCompanyId()));
                    recruiter.setCompany(company);
                }

                savedUser = recruiterRepository.save(recruiter);
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponseDTO(token, savedUser.getEmail(), savedUser.getRole().name());

    }

}
