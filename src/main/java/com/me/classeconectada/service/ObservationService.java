package com.me.classeconectada.service;

import com.me.classeconectada.model.Observation;
import com.me.classeconectada.repository.ObservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObservationService {
    private final ObservationRepository observationRepository;
    
    public List<Observation> findAll() {
        return observationRepository.findAll();
    }
    
    public Optional<Observation> findById(Long id) {
        return observationRepository.findById(id);
    }
    
    public List<Observation> findByStudentId(Long studentId) {
        return observationRepository.findByStudentId(studentId);
    }
    
    public List<Observation> findByTurmaId(Long turmaId) {
        return observationRepository.findByTurmaId(turmaId);
    }
    
    @Transactional
    public Observation save(Observation observation) {
        // Set default date if not provided
        if (observation.getDate() == null) {
            observation.setDate(LocalDate.now());
        }
        return observationRepository.save(observation);
    }
    
    @Transactional
    public Observation update(Long id, Observation observation) {
        Observation existing = observationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Observação não encontrada"));
        
        existing.setContent(observation.getContent());
        if (observation.getDate() != null) {
            existing.setDate(observation.getDate());
        }
        
        return observationRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        observationRepository.deleteById(id);
    }
}
