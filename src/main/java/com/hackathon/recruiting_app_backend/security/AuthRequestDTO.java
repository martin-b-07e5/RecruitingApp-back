package com.hackathon.recruiting_app_backend.security;

import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
