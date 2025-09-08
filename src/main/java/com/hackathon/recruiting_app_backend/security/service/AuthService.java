package com.hackathon.recruiting_app_backend.security.service;

import com.hackathon.recruiting_app_backend.model.Company;
import com.hackathon.recruiting_app_backend.model.Skill;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserCompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import com.hackathon.recruiting_app_backend.security.util.JwtUtil;
import com.hackathon.recruiting_app_backend.security.dto.RegisterRequestDTO;
import com.hackathon.recruiting_app_backend.security.dto.AuthResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, CompanyRepository companyRepository, CompanyRepository companyRepository1, UserCompanyRepository userCompanyRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository1;
        this.userCompanyRepository = userCompanyRepository;
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
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("❌ Email already exists");
        }

        // Fix: Allow RECRUITER to have companyIds, disallow for other roles
        if (!request.role().equals("RECRUITER") && request.companyIds() != null && !request.companyIds().isEmpty()) {
            throw new RuntimeException("❌ Only recruiters can be associated with companies");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .role(User.Role.valueOf(request.role()))
                .resumeFile(request.resumeFile())
                .experience(request.experience())
                .build();

        // Convert List<String> to List<Skill>
        if (request.skills() != null) {
            List<Skill> skills = request.skills().stream()
                    .map(skillName -> Skill.builder()
                            .name(skillName)
                            .user(user) // ← Establish a bidirectional relationship
                            .build())
                    .collect(Collectors.toList());
            user.setSkills(skills);
        }

        User savedUser = userRepository.save(user);

        // Link recruiter to companies
        if (request.role().equals("RECRUITER") && request.companyIds() != null && !request.companyIds().isEmpty()) {
            for (Long companyId : request.companyIds()) {
                Company company = companyRepository.findById(companyId)
                        .orElseThrow(() -> new RuntimeException("❌ Company not found" + companyId));
                if (!userCompanyRepository.existsByUserIdAndCompanyIdAndRelationshipType(
                        savedUser.getId(), company.getId(), UserCompany.EmploymentRelationshipType.RECRUITER)) {
                    userCompanyRepository.save(UserCompany.builder()
                            .user(savedUser)
                            .company(company)
                            .position("Recruiter")
                            .startDate(LocalDate.now())
                            .isCurrentlyEmployed(true)
                            .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                            .build());
                }
            }
        }
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponseDTO(token, savedUser.getEmail(), savedUser.getRole().name());
    }

}
