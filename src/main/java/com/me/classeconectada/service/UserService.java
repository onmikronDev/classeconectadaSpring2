package com.me.classeconectada.service;

import com.me.classeconectada.model.User;
import com.me.classeconectada.model.UserType;
import com.me.classeconectada.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<User> findAllActive() {
        return userRepository.findByActiveTrue();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }
    
    public List<User> findByTipo(UserType tipo) {
        return userRepository.findByTipo(tipo);
    }
    
    @Transactional
    public User save(User user) {
        // Validate email uniqueness
        if (user.getEmail() != null) {
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if provided
        if (user.getCpf() != null && !user.getCpf().isEmpty()) {
            Optional<User> existingUserByCpf = userRepository.findByCpf(user.getCpf());
            if (existingUserByCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        if (user.getActive() == null) {
            user.setActive(true);
        }
        return userRepository.save(user);
    }
    
    @Transactional
    public User update(Long id, User user) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Validate email uniqueness if email is being changed
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            Optional<User> userWithEmail = userRepository.findByEmail(user.getEmail());
            if (userWithEmail.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if CPF is being changed
        if (user.getCpf() != null && !user.getCpf().isEmpty() && !user.getCpf().equals(existingUser.getCpf())) {
            Optional<User> userWithCpf = userRepository.findByCpf(user.getCpf());
            if (userWithCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        existingUser.setNome(user.getNome());
        existingUser.setEmail(user.getEmail());
        if (user.getSenha() != null && !user.getSenha().isEmpty()) {
            existingUser.setSenha(user.getSenha());
        }
        existingUser.setCpf(user.getCpf());
        existingUser.setTelefone(user.getTelefone());
        existingUser.setEndereco(user.getEndereco());
        existingUser.setPai(user.getPai());
        existingUser.setMae(user.getMae());
        existingUser.setTipo(user.getTipo());
        
        return userRepository.save(existingUser);
    }
    
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setActive(false);
        userRepository.save(user);
    }
    
    @Transactional
    public void hardDelete(Long id) {
        userRepository.deleteById(id);
    }
    
    public User authenticate(String email, String senha) {
        Optional<User> user = userRepository.findByEmail(email);
        // Note: In production, passwords should be hashed using BCrypt or similar
        // For this demo/educational system, plain text comparison is used for simplicity
        if (user.isPresent() && user.get().getSenha().equals(senha) && user.get().getActive()) {
            return user.get();
        }
        return null;
    }
}
