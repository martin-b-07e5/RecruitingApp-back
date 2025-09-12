package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.model.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {
    boolean existsByUserIdAndCompanyIdAndRelationshipType(Long userId, Long companyId, UserCompany.EmploymentRelationshipType relationshipType);

    List<UserCompany> findByUserId(Long userId);

    List<UserCompany> findByUserIdAndRelationshipType(Long userId, UserCompany.EmploymentRelationshipType relationshipType);

    List<UserCompany> findByUser(User user);
}
