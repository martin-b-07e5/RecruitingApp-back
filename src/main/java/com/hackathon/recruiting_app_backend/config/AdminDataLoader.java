package com.hackathon.recruiting_app_backend.config;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final IUserRepository userRepository; // ✅ Cambiar por UserRepository
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@recruitingapp.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Administrator")
                    .lastName("System")
                    .phone("+1234567890")
                    .role(User.Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}