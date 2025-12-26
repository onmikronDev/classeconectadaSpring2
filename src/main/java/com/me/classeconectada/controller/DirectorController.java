package com.me.classeconectada.controller;

import com.me.classeconectada.model.Director;
import com.me.classeconectada.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;
    
    @GetMapping
    public ResponseEntity<List<Director>> getAll() {
        return ResponseEntity.ok(directorService.findAllActive());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Director> getById(@PathVariable Long id) {
        return directorService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Director> create(@Valid @RequestBody Director director) {
        try {
            Director savedDirector = directorService.save(director);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDirector);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Director> update(@PathVariable Long id, @Valid @RequestBody Director director) {
        try {
            Director updatedDirector = directorService.update(id, director);
            return ResponseEntity.ok(updatedDirector);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            directorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
