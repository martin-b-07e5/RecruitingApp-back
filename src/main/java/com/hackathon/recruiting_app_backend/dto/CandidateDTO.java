package com.hackathon.recruiting_app_backend.dto;

import lombok.Data;

    @Data
    public class CandidateDTO {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String resumeFile;
        private String skills;
        private String experience;
    }