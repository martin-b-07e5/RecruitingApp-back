package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {
    boolean existsByUserIdAndCompanyIdAndRelationshipType(Long userId, Long companyId, UserCompany.EmploymentRelationshipType relationshipType);
}
