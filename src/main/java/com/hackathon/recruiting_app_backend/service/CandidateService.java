package com.hackathon.recruiting_app_backend.service;

import com.hackathon.recruiting_app_backend.exception.ResourceNotFoundException;
import com.hackathon.recruiting_app_backend.model.Candidate;
import com.hackathon.recruiting_app_backend.repository.ICandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final ICandidateRepository repo;

    public CandidateService(ICandidateRepository repo) {
        this.repo = repo;
    }

    public List<Candidate> findAll() {
        return repo.findAll();
    }

    public Candidate findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + id));
    }

    public Candidate create(Candidate candidate) {
        return repo.save(candidate);
    }

    public Candidate update(Long id, Candidate updated) {
        Candidate existing = findById(id);
        existing.setPhone(updated.getPhone());
        existing.setResumeFile(updated.getResumeFile());
        existing.setSkills(updated.getSkills());
        existing.setExperience(updated.getExperience());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Candidate toDelete = findById(id);
        repo.delete(toDelete);
    }
}
