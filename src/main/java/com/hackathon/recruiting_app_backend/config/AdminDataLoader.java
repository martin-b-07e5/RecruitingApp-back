package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final IUserRepository userRepository; // ✅ Cambiar por UserRepository
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

            // Candidate de prueba
            User candidate = User.builder()
                    .email("candidate@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Maria")
                    .lastName("Developer")
                    .phone("+1234567892")
                    .role(User.Role.CANDIDATE)
                    .skills(List.of("Java", "Spring", "React"))
                    .experience("3 years full-stack development")
                    .resumeFile("maria_cv.pdf")
                    .build();

            userRepository.save(admin);
            userRepository.save(recruiter);
            userRepository.save(candidate);
        }

    }
}