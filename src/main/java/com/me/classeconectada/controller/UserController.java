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
            
            // Validate email uniqueness
            if (userService.findByEmail(email).isPresent()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Email já está em uso");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Validate CPF uniqueness if provided
            if (cpf != null && !cpf.isEmpty()) {
                if (userService.findByCpf(cpf).isPresent()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "CPF já está em uso");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        try {
            // Find the user first to determine the type
            User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            String nome = (String) userData.get("nome");
            String email = (String) userData.get("email");
            String senha = (String) userData.get("senha");
            String cpf = (String) userData.get("cpf");
            String telefone = (String) userData.get("telefone");
            
            // Validate email uniqueness if email is being changed
            if (email != null && !email.equals(existingUser.getEmail())) {
                if (userService.findByEmail(email).isPresent()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Email já está em uso");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }
            
            // Validate CPF uniqueness if CPF is being changed
            if (cpf != null && !cpf.isEmpty() && !cpf.equals(existingUser.getCpf())) {
                if (userService.findByCpf(cpf).isPresent()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "CPF já está em uso");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }
            
            // Update common fields
            if (nome != null) existingUser.setNome(nome);
            if (email != null) existingUser.setEmail(email);
            if (senha != null && !senha.isEmpty()) existingUser.setSenha(senha);
            if (cpf != null) existingUser.setCpf(cpf);
            if (telefone != null) existingUser.setTelefone(telefone);
            
            // Handle turmaId for Student and Teacher
            if (userData.containsKey("turmaId")) {
                if (existingUser instanceof Student) {
                    Student student = (Student) existingUser;
                    Object turmaIdObj = userData.get("turmaId");
                    if (turmaIdObj != null) {
                        Long turmaId = turmaIdObj instanceof Number ? ((Number) turmaIdObj).longValue() : Long.parseLong(turmaIdObj.toString());
                        SchoolClass turma = schoolClassRepository.findById(turmaId)
                            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
                        student.setTurma(turma);
                    } else {
                        student.setTurma(null);
                    }
                } else if (existingUser instanceof Teacher) {
                    Teacher teacher = (Teacher) existingUser;
                    Object turmaIdObj = userData.get("turmaId");
                    if (turmaIdObj != null) {
                        Long turmaId = turmaIdObj instanceof Number ? ((Number) turmaIdObj).longValue() : Long.parseLong(turmaIdObj.toString());
                        SchoolClass turma = schoolClassRepository.findById(turmaId)
                            .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
                        teacher.setTurma(turma);
                    } else {
                        teacher.setTurma(null);
                    }
                }
            }
            
            // Save using the appropriate repository
            User updatedUser;
            if (existingUser instanceof Student) {
                updatedUser = studentRepository.save((Student) existingUser);
            } else if (existingUser instanceof Teacher) {
                updatedUser = teacherRepository.save((Teacher) existingUser);
            } else if (existingUser instanceof Director) {
                updatedUser = directorRepository.save((Director) existingUser);
            } else {
                updatedUser = userService.save(existingUser);
            }
            
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            // Soft delete by setting active to false
            user.setActive(false);
            
            // Save using the appropriate repository
            if (user instanceof Student) {
                studentRepository.save((Student) user);
            } else if (user instanceof Teacher) {
                teacherRepository.save((Teacher) user);
            } else if (user instanceof Director) {
                directorRepository.save((Director) user);
            } else {
                userService.save(user);
            }
            
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao deletar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
