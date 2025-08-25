package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

/* This DTO is only responsible for returning the response after login/registration and contains:
    ✅ JWT token
    ✅ User email
    ✅ User role
*/
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
