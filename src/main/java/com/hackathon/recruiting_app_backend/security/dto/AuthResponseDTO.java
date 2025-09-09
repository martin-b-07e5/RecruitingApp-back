package com.hackathon.recruiting_app_backend.security.dto;

import lombok.Data;

/* This DTO is only responsible for returning the response after login/registration and contains:
    ✅ JWT token
    ✅ User email
    ✅ User role
*/

/* Data is used because this is a class and not a record */
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
