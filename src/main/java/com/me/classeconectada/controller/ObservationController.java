package com.me.classeconectada.controller;

import com.me.classeconectada.model.Observation;
import com.me.classeconectada.service.ObservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ObservationController {
    private final ObservationService observationService;
    
    @GetMapping
    public ResponseEntity<List<Observation>> getAll() {
        return ResponseEntity.ok(observationService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Observation> getById(@PathVariable Long id) {
        return observationService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Observation>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(observationService.findByStudentId(studentId));
    }
    
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<Observation>> getByTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok(observationService.findByTurmaId(turmaId));
    }
    
    @PostMapping
    public ResponseEntity<Observation> create(@Valid @RequestBody Observation observation) {
        try {
            Observation savedObservation = observationService.save(observation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedObservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Observation> update(@PathVariable Long id, @Valid @RequestBody Observation observation) {
        try {
            Observation updatedObservation = observationService.update(id, observation);
            return ResponseEntity.ok(updatedObservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            observationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
