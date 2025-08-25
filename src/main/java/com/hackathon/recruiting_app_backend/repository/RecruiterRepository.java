package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByEmail(String email);
}
