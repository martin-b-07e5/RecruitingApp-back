package com.hackathon.recruiting_app_backend.security.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
