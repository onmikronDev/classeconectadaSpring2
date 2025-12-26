package com.me.classeconectada.service;

import com.me.classeconectada.model.Teacher;
import com.me.classeconectada.repository.TeacherRepository;
import com.me.classeconectada.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
    
    public List<Teacher> findAllActive() {
        return teacherRepository.findByActiveTrue();
    }
    
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }
    
    public List<Teacher> findByTurmaId(Long turmaId) {
        return teacherRepository.findByTurmaId(turmaId);
    }
    
    @Transactional
    public Teacher save(Teacher teacher) {
        // Validate email uniqueness
        if (teacher.getEmail() != null) {
            Optional<com.me.classeconectada.model.User> existingUser = userRepository.findByEmail(teacher.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if provided
        if (teacher.getCpf() != null && !teacher.getCpf().isEmpty()) {
            Optional<com.me.classeconectada.model.User> existingUserByCpf = userRepository.findByCpf(teacher.getCpf());
            if (existingUserByCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        if (teacher.getActive() == null) {
            teacher.setActive(true);
        }
        return teacherRepository.save(teacher);
    }
    
    @Transactional
    public Teacher update(Long id, Teacher teacher) {
        Teacher existing = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
        // Validate email uniqueness if email is being changed
        if (teacher.getEmail() != null && !teacher.getEmail().equals(existing.getEmail())) {
            Optional<com.me.classeconectada.model.User> userWithEmail = userRepository.findByEmail(teacher.getEmail());
            if (userWithEmail.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if CPF is being changed
        if (teacher.getCpf() != null && !teacher.getCpf().isEmpty() && !teacher.getCpf().equals(existing.getCpf())) {
            Optional<com.me.classeconectada.model.User> userWithCpf = userRepository.findByCpf(teacher.getCpf());
            if (userWithCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        existing.setNome(teacher.getNome());
        existing.setEmail(teacher.getEmail());
        if (teacher.getSenha() != null && !teacher.getSenha().isEmpty()) {
            existing.setSenha(teacher.getSenha());
        }
        existing.setCpf(teacher.getCpf());
        existing.setTelefone(teacher.getTelefone());
        existing.setEndereco(teacher.getEndereco());
        existing.setPai(teacher.getPai());
        existing.setMae(teacher.getMae());
        existing.setTurma(teacher.getTurma());
        
        return teacherRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        teacher.setActive(false);
        teacherRepository.save(teacher);
    }
}
