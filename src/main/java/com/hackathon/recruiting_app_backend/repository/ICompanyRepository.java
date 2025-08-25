package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);
}
