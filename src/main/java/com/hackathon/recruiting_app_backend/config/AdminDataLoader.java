package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.Admin;
import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // Populates the database with sample companies
    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            // Create main admin user
            Admin admin = Admin.builder()
                    .email("admin@recruitingapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Administrator")
                    .lastName("System")
                    .phone("+1234567890")
                    .role(User.Role.ADMIN)
                    .resumeFile("resume1.pdf")
                    .skills("Java, Spring, Hibernate")
                    .experience("5 year")
                    .build();

            // Create additional admin
            Admin martin = Admin.builder()
                    .email("martin@recruitingapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Martin")
                    .lastName("Admin")
                    .phone("+1122334455")
                    .role(User.Role.ADMIN)
                    .resumeFile("resume2.pdf")
                    .skills("Java, Spring, Hibernate")
                    .experience("3 year")
                    .build();

            // Create another admin
            Admin haylen = Admin.builder()
                    .email("haylen@recruitingapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Haylen")
                    .lastName("Admin")
                    .phone("+0987654321")
                    .role(User.Role.ADMIN)
                    .resumeFile("resume3.pdf")
                    .skills("Java, Spring, Hibernate")
                    .experience("1 year")
                    .build();

            adminRepository.save(admin);
            adminRepository.save(haylen);
            adminRepository.save(martin);
        }
    }
}
