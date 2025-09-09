package com.hackathon.recruiting_app_backend.security.dto;

import lombok.Data;

/* Data is used because this is a class and not a record */
@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
