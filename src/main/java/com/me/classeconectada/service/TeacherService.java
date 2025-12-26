package com.me.classeconectada.service;

import com.me.classeconectada.model.Teacher;
import com.me.classeconectada.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    
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
        if (teacher.getActive() == null) {
            teacher.setActive(true);
        }
        return teacherRepository.save(teacher);
    }
    
    @Transactional
    public Teacher update(Long id, Teacher teacher) {
        Teacher existing = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
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
