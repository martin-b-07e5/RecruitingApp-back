package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId);
}
