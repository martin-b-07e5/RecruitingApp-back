package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.IUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found after authentication"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

//        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());

    }

    public AuthResponseDTO register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());

//        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponseDTO(token, savedUser.getEmail(), savedUser.getRole().name());

    }


}
