package com.hackathon.recruiting_app_backend.security.controller;

import com.hackathon.recruiting_app_backend.security.dto.AuthRequestDTO;
import com.hackathon.recruiting_app_backend.security.dto.AuthResponseDTO;
import com.hackathon.recruiting_app_backend.security.service.AuthService;
import com.hackathon.recruiting_app_backend.security.dto.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

}
