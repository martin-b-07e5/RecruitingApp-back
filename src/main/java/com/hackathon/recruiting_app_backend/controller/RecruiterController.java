package com.hackathon.recruiting_app_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {
    
    @GetMapping("/profile")
    @PreAuthorize("hasRole('RECRUITER')")
    public String getProfile() {
        return "Recruiter profile works!";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('RECRUITER')")
    public String getDashboard() {
        return "Recruiter dashboard works!";
    }
}
