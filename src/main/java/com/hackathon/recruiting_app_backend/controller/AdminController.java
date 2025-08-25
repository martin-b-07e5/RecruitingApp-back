package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.model.Admin;
import com.hackathon.recruiting_app_backend.repository.IAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminRepository adminRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public String getProfile() {
        return "Admin profile works!";
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

}
