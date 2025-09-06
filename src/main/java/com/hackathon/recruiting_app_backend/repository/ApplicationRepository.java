package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobOfferIdAndCandidateId(Long jobOfferId, Long candidateId);
}
