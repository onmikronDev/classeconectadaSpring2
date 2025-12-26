package com.me.classeconectada.service;

import com.me.classeconectada.model.Director;
import com.me.classeconectada.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;
    
    public List<Director> findAll() {
        return directorRepository.findAll();
    }
    
    public List<Director> findAllActive() {
        return directorRepository.findByActiveTrue();
    }
    
    public Optional<Director> findById(Long id) {
        return directorRepository.findById(id);
    }
    
    @Transactional
    public Director save(Director director) {
        if (director.getActive() == null) {
            director.setActive(true);
        }
        return directorRepository.save(director);
    }
    
    @Transactional
    public Director update(Long id, Director director) {
        Director existing = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diretor não encontrado"));
        
        existing.setNome(director.getNome());
        existing.setEmail(director.getEmail());
        if (director.getSenha() != null && !director.getSenha().isEmpty()) {
            existing.setSenha(director.getSenha());
        }
        existing.setCpf(director.getCpf());
        existing.setTelefone(director.getTelefone());
        existing.setEndereco(director.getEndereco());
        existing.setPai(director.getPai());
        existing.setMae(director.getMae());
        existing.setSetor(director.getSetor());
        
        return directorRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        Director director = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diretor não encontrado"));
        director.setActive(false);
        directorRepository.save(director);
    }
}
