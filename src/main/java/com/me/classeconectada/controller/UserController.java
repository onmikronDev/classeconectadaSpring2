package com.me.classeconectada.controller;

import com.me.classeconectada.model.*;
import com.me.classeconectada.repository.SchoolClassRepository;
import com.me.classeconectada.repository.StudentRepository;
import com.me.classeconectada.repository.TeacherRepository;
import com.me.classeconectada.repository.DirectorRepository;
import com.me.classeconectada.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DirectorRepository directorRepository;
    
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAllActive());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<User>> getByTipo(@PathVariable String tipo) {
        try {
            UserType userType = UserType.valueOf(tipo.toUpperCase());
            return ResponseEntity.ok(userService.findByTipo(userType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> userData) {
        // ✅ CORRIGIDO: Aceita Map para processar turmaId do JSON
        try {
            String nome = (String) userData.get("nome");
            String email = (String) userData.get("email");
            String senha = (String) userData.get("senha");
            String cpf = (String) userData.get("cpf");
            String telefone = (String) userData.get("telefone");
            String tipoStr = (String) userData.get("tipo");
            
            if (nome == null || email == null || senha == null || tipoStr == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Campos obrigatórios: nome, email, senha, tipo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            UserType tipo = UserType.valueOf(tipoStr.toUpperCase());
            
            // Create appropriate user type based on tipo
            User user;
            if (tipo == UserType.ALUNO) {
                Student student = new Student();
                student.setNome(nome);
                student.setEmail(email);
                student.setSenha(senha);
                student.setCpf(cpf);
                student.setTelefone(telefone);
                student.setTipo(tipo);
                student.setActive(true);
                
                // ✅ CORRIGIDO: Vincular turma se turmaId fornecido
                if (userData.containsKey("turmaId") && userData.get("turmaId") != null) {
                    Object turmaIdObj = userData.get("turmaId");
                    Long turmaId = turmaIdObj instanceof Number ? ((Number) turmaIdObj).longValue() : Long.parseLong(turmaIdObj.toString());
                    SchoolClass turma = schoolClassRepository.findById(turmaId)
                        .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
                    student.setTurma(turma);
                }
                
                user = studentRepository.save(student);
            } else if (tipo == UserType.PROFESSOR) {
                Teacher teacher = new Teacher();
                teacher.setNome(nome);
                teacher.setEmail(email);
                teacher.setSenha(senha);
                teacher.setCpf(cpf);
                teacher.setTelefone(telefone);
                teacher.setTipo(tipo);
                teacher.setActive(true);
                
                // ✅ CORRIGIDO: Vincular turma se turmaId fornecido
                if (userData.containsKey("turmaId") && userData.get("turmaId") != null) {
                    Object turmaIdObj = userData.get("turmaId");
                    Long turmaId = turmaIdObj instanceof Number ? ((Number) turmaIdObj).longValue() : Long.parseLong(turmaIdObj.toString());
                    SchoolClass turma = schoolClassRepository.findById(turmaId)
                        .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
                    teacher.setTurma(turma);
                }
                
                user = teacherRepository.save(teacher);
            } else if (tipo == UserType.DIRETOR) {
                Director director = new Director();
                director.setNome(nome);
                director.setEmail(email);
                director.setSenha(senha);
                director.setCpf(cpf);
                director.setTelefone(telefone);
                director.setTipo(tipo);
                director.setActive(true);
                
                user = directorRepository.save(director);
            } else {
                user = new User();
                user.setNome(nome);
                user.setEmail(email);
                user.setSenha(senha);
                user.setCpf(cpf);
                user.setTelefone(telefone);
                user.setTipo(tipo);
                user.setActive(true);
                user = userService.save(user);
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao criar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
