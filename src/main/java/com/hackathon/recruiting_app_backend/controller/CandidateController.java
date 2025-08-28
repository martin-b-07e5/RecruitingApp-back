package com.hackathon.recruiting_app_backend.controller;

import com.hackathon.recruiting_app_backend.dto.CandidateDTO;
import com.hackathon.recruiting_app_backend.model.Candidate;
import com.hackathon.recruiting_app_backend.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    // Utility methods to map between DTO and Entity
    private CandidateDTO toDTO(Candidate c) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(c.getId());
        dto.setEmail(c.getEmail());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setPhone(c.getPhone());
        dto.setResumeFile(c.getResumeFile());
        dto.setSkills(c.getSkills());
        dto.setExperience(c.getExperience());
        return dto;
    }

    private Candidate toEntity(CandidateDTO dto) {
        Candidate c = new Candidate();
        c.setEmail(dto.getEmail());
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setPhone(dto.getPhone());
        c.setResumeFile(dto.getResumeFile());
        c.setSkills(dto.getSkills());
        c.setExperience(dto.getExperience());
        return c;
    }

    @GetMapping
    public List<CandidateDTO> getAll() {
        return service.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getById(@PathVariable Long id) {
        Candidate c = service.findById(id);
        return ResponseEntity.ok(toDTO(c));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CandidateDTO> create(@RequestBody CandidateDTO dto) {
        Candidate saved = service.create(toEntity(dto));
        return ResponseEntity.ok(toDTO(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> update(@PathVariable Long id,
                                               @RequestBody CandidateDTO dto) {
        Candidate updated = service.update(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
