package com.me.classeconectada.service;

import com.me.classeconectada.model.Student;
import com.me.classeconectada.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    
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
        if (student.getActive() == null) {
            student.setActive(true);
        }
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student update(Long id, Student student) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
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
