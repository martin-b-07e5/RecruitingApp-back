package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.dto.UserRequestUpdateDTO;
import com.hackathon.recruiting_app_backend.model.Skill;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import com.hackathon.recruiting_app_backend.repository.CompanyRepository;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    // dependency injection
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    // updateUser (users can update their own details and admins can update any user.)
    public void updateUser(Long targetUserId, Long authenticatedUserId, User.Role role, UserRequestUpdateDTO userRequestUpdateDTO) {

        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));

        // Authorization check: Only admins or the user themselves can update
        if (role != User.Role.ADMIN && !authenticatedUserId.equals(targetUserId)) {
            throw new RuntimeException("You are not authorized to update this user");
        }

        // Email uniqueness check
        if (userRequestUpdateDTO.email() != null && !userRequestUpdateDTO.email().equals(targetUser.getEmail())) {
            if (userRepository.existsByEmail(userRequestUpdateDTO.email())) {
                throw new RuntimeException("Email already exists");
            }
            targetUser.setEmail(userRequestUpdateDTO.email());
        }
        if (userRequestUpdateDTO.password() != null && !userRequestUpdateDTO.password().isEmpty()) {
            targetUser.setPassword(passwordEncoder.encode(userRequestUpdateDTO.password()));
        }
        if (userRequestUpdateDTO.firstName() != null && !userRequestUpdateDTO.firstName().isEmpty()) {
            targetUser.setFirstName(userRequestUpdateDTO.firstName());
        }
        if (userRequestUpdateDTO.lastName() != null && !userRequestUpdateDTO.lastName().isEmpty()) {
            targetUser.setLastName(userRequestUpdateDTO.lastName());
        }
        if (userRequestUpdateDTO.phone() != null) {
            targetUser.setPhone(userRequestUpdateDTO.phone());
        }
        if (userRequestUpdateDTO.resumeFile() != null) {
            targetUser.setResumeFile(userRequestUpdateDTO.resumeFile());
        }
        if (userRequestUpdateDTO.experience() != null) {
            targetUser.setExperience(userRequestUpdateDTO.experience());
        }

        // Update company associations (for recruiters only)
        if (userRequestUpdateDTO.companyIds() != null && !userRequestUpdateDTO.companyIds().isEmpty()) {

            if (targetUser.getRole() != User.Role.RECRUITER) {
                throw new RuntimeException("Only recruiters can be associated with companies");
            }

            // Clear existing companies to avoid duplicates
            targetUser.getCompanies().clear();

            List<UserCompany> newCompanies = userRequestUpdateDTO.companyIds().stream()
                    .map(companyId -> companyRepository.findById(companyId)
                            .orElseThrow(() -> new RuntimeException("Company not found: ID " + companyId)))
                    .map(company -> UserCompany.builder()
                            .user(targetUser)
                            .company(company)
                            .relationshipType(UserCompany.EmploymentRelationshipType.RECRUITER)
                            .position("Recruiter")
                            .startDate(LocalDate.now())
                            .isCurrentlyEmployed(true)
                            .build())
                    .toList();

//            targetUser.setCompanies(newCompanies); // replaced the list but didn’t persist correctly due to Hibernate’s handling. Adding clear() and addAll() ensures proper updates.
            targetUser.getCompanies().addAll(newCompanies);  // Assign new companies to update user_company table
        }

        // Update skills (for all roles)
        if (userRequestUpdateDTO.skills() != null && !userRequestUpdateDTO.skills().isEmpty()) {
            // Clear existing skills to avoid duplicates
            targetUser.getSkills().clear();

            List<Skill> newSkills = userRequestUpdateDTO.skills().stream()
                    .map(skillName -> Skill.builder()
                            .name(skillName)
                            .user(targetUser)
                            .build())
                    .toList();

            targetUser.getSkills().addAll(newSkills); // update user_skills table
        }

        userRepository.save(targetUser);
    }

    // deleteUser
    public void deleteUser(Long userId, User.Role role) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Authorization check first
        if (role != User.Role.ADMIN && !user.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this user");
        }

        userRepository.delete(user);
    }

}
