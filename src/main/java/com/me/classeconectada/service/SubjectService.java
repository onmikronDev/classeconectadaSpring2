package com.me.classeconectada.service;

import com.me.classeconectada.model.Subject;
import com.me.classeconectada.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }
    
    public List<Subject> findAllActive() {
        return subjectRepository.findByActiveTrue();
    }
    
    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }
    
    public List<Subject> findByTeacherId(Long teacherId) {
        return subjectRepository.findByTeacherId(teacherId);
    }
    
    @Transactional
    public Subject save(Subject subject) {
        if (subject.getActive() == null) {
            subject.setActive(true);
        }
        return subjectRepository.save(subject);
    }
    
    @Transactional
    public Subject update(Long id, Subject subject) {
        Subject existing = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
        
        existing.setName(subject.getName());
        existing.setTeacher(subject.getTeacher());
        
        return subjectRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
        subject.setActive(false);
        subjectRepository.save(subject);
    }
}
