package com.hackathon.recruiting_app_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public String getProfile() {
        return "Admin profile works!";
    }
}
