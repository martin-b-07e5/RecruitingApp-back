package com.hackathon.recruiting_app_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {


    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String getProfile() {
        return "Candidate profile works!";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String getDashboard() {
        return "Candidate dashboard works!";
    }

}
