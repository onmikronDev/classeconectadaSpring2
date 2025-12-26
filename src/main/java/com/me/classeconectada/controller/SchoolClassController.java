package com.me.classeconectada.controller;

import com.me.classeconectada.model.SchoolClass;
import com.me.classeconectada.model.Student;
import com.me.classeconectada.service.SchoolClassService;
import com.me.classeconectada.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SchoolClassController {
    private final SchoolClassService schoolClassService;
    private final StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<SchoolClass>> getAll() {
        return ResponseEntity.ok(schoolClassService.findAllActive());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SchoolClass> getById(@PathVariable Long id) {
        return schoolClassService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findByTurmaId(id));
    }
    
    @PostMapping
    public ResponseEntity<SchoolClass> create(@Valid @RequestBody SchoolClass schoolClass) {
        try {
            SchoolClass savedSchoolClass = schoolClassService.save(schoolClass);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSchoolClass);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SchoolClass> update(@PathVariable Long id, @Valid @RequestBody SchoolClass schoolClass) {
        try {
            SchoolClass updatedSchoolClass = schoolClassService.update(id, schoolClass);
            return ResponseEntity.ok(updatedSchoolClass);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            schoolClassService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
