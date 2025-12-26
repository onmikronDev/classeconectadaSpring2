package com.me.classeconectada.service;

import com.me.classeconectada.model.Student;
import com.me.classeconectada.repository.StudentRepository;
import com.me.classeconectada.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public List<Student> findAllActive() {
        return studentRepository.findByActiveTrue();
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public List<Student> findByTurmaId(Long turmaId) {
        return studentRepository.findByTurmaId(turmaId);
    }
    
    @Transactional
    public Student save(Student student) {
        // Validate email uniqueness
        if (student.getEmail() != null) {
            Optional<com.me.classeconectada.model.User> existingUser = userRepository.findByEmail(student.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if provided
        if (student.getCpf() != null && !student.getCpf().isEmpty()) {
            Optional<com.me.classeconectada.model.User> existingUserByCpf = userRepository.findByCpf(student.getCpf());
            if (existingUserByCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        if (student.getActive() == null) {
            student.setActive(true);
        }
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student update(Long id, Student student) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        // Validate email uniqueness if email is being changed
        if (student.getEmail() != null && !student.getEmail().equals(existing.getEmail())) {
            Optional<com.me.classeconectada.model.User> userWithEmail = userRepository.findByEmail(student.getEmail());
            if (userWithEmail.isPresent()) {
                throw new IllegalArgumentException("Email já está em uso");
            }
        }
        
        // Validate CPF uniqueness if CPF is being changed
        if (student.getCpf() != null && !student.getCpf().isEmpty() && !student.getCpf().equals(existing.getCpf())) {
            Optional<com.me.classeconectada.model.User> userWithCpf = userRepository.findByCpf(student.getCpf());
            if (userWithCpf.isPresent()) {
                throw new IllegalArgumentException("CPF já está em uso");
            }
        }
        
        existing.setNome(student.getNome());
        existing.setEmail(student.getEmail());
        if (student.getSenha() != null && !student.getSenha().isEmpty()) {
            existing.setSenha(student.getSenha());
        }
        existing.setCpf(student.getCpf());
        existing.setTelefone(student.getTelefone());
        existing.setEndereco(student.getEndereco());
        existing.setPai(student.getPai());
        existing.setMae(student.getMae());
        existing.setTurma(student.getTurma());
        
        return studentRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        student.setActive(false);
        studentRepository.save(student);
    }
}
