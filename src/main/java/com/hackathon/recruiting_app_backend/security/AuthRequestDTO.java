package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
