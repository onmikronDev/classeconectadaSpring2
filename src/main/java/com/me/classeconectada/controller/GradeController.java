package com.me.classeconectada.controller;

import com.me.classeconectada.dto.GradeDTO;
import com.me.classeconectada.model.Grade;
import com.me.classeconectada.model.Student;
import com.me.classeconectada.model.Subject;
import com.me.classeconectada.repository.StudentRepository;
import com.me.classeconectada.repository.SubjectRepository;
import com.me.classeconectada.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    @GetMapping
    public ResponseEntity<List<Grade>> getAll() {
        return ResponseEntity.ok(gradeService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getById(@PathVariable Long id) {
        return gradeService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.findByStudentId(studentId));
    }
    
    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public ResponseEntity<List<Grade>> getByStudentAndSubject(
            @PathVariable Long studentId, 
            @PathVariable Long subjectId) {
        return ResponseEntity.ok(gradeService.findByStudentIdAndSubjectId(studentId, subjectId));
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Grade>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(gradeService.findBySubjectId(subjectId));
    }
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody GradeDTO gradeDTO) {
        // ✅ CORRIGIDO: Aceita GradeDTO com studentId e subjectId
        try {
            Student student = studentRepository.findById(gradeDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            
            Subject subject = subjectRepository.findById(gradeDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
            
            Grade grade = new Grade();
            grade.setStudent(student);
            grade.setSubject(subject);
            grade.setValue(gradeDTO.getValue());
            grade.setDescription(gradeDTO.getDescription());
            
            // Parse examDate if provided
            if (gradeDTO.getExamDate() != null && !gradeDTO.getExamDate().isEmpty()) {
                grade.setExamDate(LocalDate.parse(gradeDTO.getExamDate()));
            } else {
                grade.setExamDate(LocalDate.now());
            }
            
            Grade savedGrade = gradeService.save(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao processar requisição: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Grade> update(@PathVariable Long id, @Valid @RequestBody Grade grade) {
        try {
            Grade updatedGrade = gradeService.update(id, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            gradeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
