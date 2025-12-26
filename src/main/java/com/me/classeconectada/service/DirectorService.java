package com.me.classeconectada.service;

import com.me.classeconectada.model.Director;
import com.me.classeconectada.repository.DirectorRepository;
import com.me.classeconectada.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;
    private final UserRepository userRepository;
    
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
        // Validate email uniqueness
        if (director.getEmail() != null) {
            Optional<com.me.classeconectada.model.User> existingUser = userRepository.findByEmail(director.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if provided
        if (director.getCpf() != null && !director.getCpf().isEmpty()) {
            Optional<com.me.classeconectada.model.User> existingUserByCpf = userRepository.findByCpf(director.getCpf());
            if (existingUserByCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        if (director.getActive() == null) {
            director.setActive(true);
        }
        return directorRepository.save(director);
    }
    
    @Transactional
    public Director update(Long id, Director director) {
        Director existing = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diretor não encontrado"));
        
        // Validate email uniqueness if email is being changed
        if (director.getEmail() != null && !director.getEmail().equals(existing.getEmail())) {
            Optional<com.me.classeconectada.model.User> userWithEmail = userRepository.findByEmail(director.getEmail());
            if (userWithEmail.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if CPF is being changed
        if (director.getCpf() != null && !director.getCpf().isEmpty() && !director.getCpf().equals(existing.getCpf())) {
            Optional<com.me.classeconectada.model.User> userWithCpf = userRepository.findByCpf(director.getCpf());
            if (userWithCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
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
