package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String email;
    private String role;

    public AuthResponseDTO(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
