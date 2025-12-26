package com.me.classeconectada.controller;

import com.me.classeconectada.model.Director;
import com.me.classeconectada.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> create(@Valid @RequestBody Director director) {
        try {
            Director savedDirector = directorService.save(director);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDirector);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao criar diretor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Director director) {
        try {
            Director updatedDirector = directorService.update(id, director);
            return ResponseEntity.ok(updatedDirector);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
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
