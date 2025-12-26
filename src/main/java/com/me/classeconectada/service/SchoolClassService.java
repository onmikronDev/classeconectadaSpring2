package com.me.classeconectada.service;

import com.me.classeconectada.model.SchoolClass;
import com.me.classeconectada.repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;
    
    public List<SchoolClass> findAll() {
        return schoolClassRepository.findAll();
    }
    
    public List<SchoolClass> findAllActive() {
        return schoolClassRepository.findByActiveTrue();
    }
    
    public Optional<SchoolClass> findById(Long id) {
        return schoolClassRepository.findById(id);
    }
    
    @Transactional
    public SchoolClass save(SchoolClass schoolClass) {
        if (schoolClass.getActive() == null) {
            schoolClass.setActive(true);
        }
        return schoolClassRepository.save(schoolClass);
    }
    
    @Transactional
    public SchoolClass update(Long id, SchoolClass schoolClass) {
        SchoolClass existing = schoolClassRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        
        existing.setName(schoolClass.getName());
        
        return schoolClassRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        schoolClass.setActive(false);
        schoolClassRepository.save(schoolClass);
    }
}
