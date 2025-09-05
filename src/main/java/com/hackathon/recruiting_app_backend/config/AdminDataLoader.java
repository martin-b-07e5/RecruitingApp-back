package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.Skill;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Order(1) // Run first
public class AdminDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Admin.
            User admin = User.builder()
                    .email("admin@recruitingapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Administrator")
                    .lastName("System")
                    .phone("+1234567890")
                    .role(User.Role.ADMIN)
                    .build();

            // Test recruiter.
            User recruiter = User.builder()
                    .email("recruiter@google.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("John")
                    .lastName("Recruiter")
                    .phone("+1234567891")
                    .role(User.Role.RECRUITER)
                    .build();

            // Test Candidate
            User candidate = User.builder()
                    .email("candidate@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Maria")
                    .lastName("Developer")
                    .phone("+1234567892")
                    .role(User.Role.CANDIDATE)
//                    .skills(List.of("Java", "Spring", "React"))
                    .experience("3 years full-stack development")
                    .resumeFile("maria_cv.pdf")
                    .build();

            // Skills of the candidate@example.com
            candidate.setSkills(Stream.of("Java", "Spring", "React")
                    .map(name -> Skill.builder()
                            .name(name)
                            .user(candidate)
                            .build())
                    .collect(Collectors.toList()));

            userRepository.save(admin);
            userRepository.save(recruiter);
            userRepository.save(candidate);
        }
    }

}