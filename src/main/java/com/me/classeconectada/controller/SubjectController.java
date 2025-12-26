package com.me.classeconectada.controller;

import com.me.classeconectada.model.Subject;
import com.me.classeconectada.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    
    @GetMapping
    public ResponseEntity<List<Subject>> getAll() {
        return ResponseEntity.ok(subjectService.findAllActive());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getById(@PathVariable Long id) {
        return subjectService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Subject>> getByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectService.findByTeacherId(teacherId));
    }
    
    @PostMapping
    public ResponseEntity<Subject> create(@Valid @RequestBody Subject subject) {
        try {
            Subject savedSubject = subjectService.save(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Subject> update(@PathVariable Long id, @Valid @RequestBody Subject subject) {
        try {
            Subject updatedSubject = subjectService.update(id, subject);
            return ResponseEntity.ok(updatedSubject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            subjectService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
