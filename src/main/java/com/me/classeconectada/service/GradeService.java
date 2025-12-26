package com.me.classeconectada.service;

import com.me.classeconectada.model.Grade;
import com.me.classeconectada.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
    
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }
    
    public Optional<Grade> findById(Long id) {
        return gradeRepository.findById(id);
    }
    
    public List<Grade> findByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    public List<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        return gradeRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }
    
    public List<Grade> findBySubjectId(Long subjectId) {
        return gradeRepository.findBySubjectId(subjectId);
    }
    
    @Transactional
    public Grade save(Grade grade) {
        validateGrade(grade.getValue());
        return gradeRepository.save(grade);
    }
    
    @Transactional
    public Grade update(Long id, Grade grade) {
        Grade existing = gradeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        
        validateGrade(grade.getValue());
        existing.setValue(grade.getValue());
        existing.setDescription(grade.getDescription());
        existing.setExamDate(grade.getExamDate());
        
        return gradeRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        gradeRepository.deleteById(id);
    }
    
    private void validateGrade(Double value) {
        if (value < 0 || value > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
        }
    }
}
