package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
}
