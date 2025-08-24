package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.User;
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

/* // 🔍 The login response is incomplete
// The "ROLE" is hardcoded - you need to get the actual user's role from the database.
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        // You'll need to add user details lookup here for the response
        return ResponseEntity.ok(new AuthResponseDTO(token, request.getEmail(), "ROLE"));
    }*/

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody User user) {
//        String token = authService.register(user);
        AuthResponseDTO response = authService.register(user);
//        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getRole().name()));
        return ResponseEntity.ok(response);
    }


}
