package com.me.classeconectada.controller;

import com.me.classeconectada.model.*;
import com.me.classeconectada.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @BeforeEach
    void setUp() {
        // @Transactional annotation will rollback changes after each test
        // No need to manually delete data
    }

    @Test
    void testCreateStudent_Success() {
        Student student = new Student();
        student.setNome("Test Student");
        student.setEmail("student@test.com");
        student.setSenha("password123");
        student.setCpf("12345678900");
        student.setTelefone("1234567890");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);

        Student saved = studentRepository.save(student);
        
        assertNotNull(saved.getId());
        assertEquals("Test Student", saved.getNome());
        assertEquals("student@test.com", saved.getEmail());
        assertEquals(UserType.ALUNO, saved.getTipo());
        assertTrue(saved.getActive());
    }

    @Test
    void testCreateMultipleUsers_Success() {
        // Create first student
        Student student1 = new Student();
        student1.setNome("Student One");
        student1.setEmail("student1@test.com");
        student1.setSenha("password123");
        student1.setCpf("11111111111");
        student1.setTipo(UserType.ALUNO);
        student1.setActive(true);
        studentRepository.save(student1);

        // Create second student with different email and CPF
        Student student2 = new Student();
        student2.setNome("Student Two");
        student2.setEmail("student2@test.com");
        student2.setSenha("password456");
        student2.setCpf("22222222222");
        student2.setTipo(UserType.ALUNO);
        student2.setActive(true);
        studentRepository.save(student2);

        // Verify both are saved (at least 2)
        assertTrue(studentRepository.findAll().size() >= 2);
    }

    @Test
    void testCreateTeacher_Success() {
        Teacher teacher = new Teacher();
        teacher.setNome("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setSenha("password123");
        teacher.setCpf("98765432100");
        teacher.setTelefone("0987654321");
        teacher.setTipo(UserType.PROFESSOR);
        teacher.setActive(true);

        Teacher saved = teacherRepository.save(teacher);
        
        assertNotNull(saved.getId());
        assertEquals("Test Teacher", saved.getNome());
        assertEquals("teacher@test.com", saved.getEmail());
        assertEquals(UserType.PROFESSOR, saved.getTipo());
    }

    @Test
    void testCreateDirector_Success() {
        Director director = new Director();
        director.setNome("Test Director");
        director.setEmail("director@test.com");
        director.setSenha("password123");
        director.setTipo(UserType.DIRETOR);
        director.setActive(true);

        Director saved = directorRepository.save(director);
        
        assertNotNull(saved.getId());
        assertEquals("Test Director", saved.getNome());
        assertEquals("director@test.com", saved.getEmail());
        assertEquals(UserType.DIRETOR, saved.getTipo());
    }

    @Test
    void testUpdateStudent_Success() {
        // Create a student
        Student student = new Student();
        student.setNome("Original Name");
        student.setEmail("original@test.com");
        student.setSenha("password123");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);
        student = studentRepository.save(student);

        // Update the student
        student.setNome("Updated Name");
        student.setEmail("updated@test.com");
        Student updated = studentRepository.save(student);
        
        assertEquals("Updated Name", updated.getNome());
        assertEquals("updated@test.com", updated.getEmail());
    }

    @Test
    void testUpdateTeacher_Success() {
        // Create a teacher
        Teacher teacher = new Teacher();
        teacher.setNome("Original Teacher");
        teacher.setEmail("originalteacher@test.com");
        teacher.setSenha("password123");
        teacher.setTipo(UserType.PROFESSOR);
        teacher.setActive(true);
        teacher = teacherRepository.save(teacher);

        // Update the teacher
        teacher.setNome("Updated Teacher");
        teacher.setTelefone("1234567890");
        Teacher updated = teacherRepository.save(teacher);
        
        assertEquals("Updated Teacher", updated.getNome());
        assertEquals("1234567890", updated.getTelefone());
    }

    @Test
    void testSoftDeleteStudent_Success() {
        // Create a student
        Student student = new Student();
        student.setNome("Test Student");
        student.setEmail("delete@test.com");
        student.setSenha("password123");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);
        student = studentRepository.save(student);

        // Soft delete the student
        student.setActive(false);
        studentRepository.save(student);

        // Verify the student is soft-deleted
        Student deletedStudent = studentRepository.findById(student.getId()).orElse(null);
        assertNotNull(deletedStudent);
        assertFalse(deletedStudent.getActive());
    }

    @Test
    void testSoftDeleteTeacher_Success() {
        // Create a teacher
        Teacher teacher = new Teacher();
        teacher.setNome("Test Teacher");
        teacher.setEmail("deleteteacher@test.com");
        teacher.setSenha("password123");
        teacher.setTipo(UserType.PROFESSOR);
        teacher.setActive(true);
        teacher = teacherRepository.save(teacher);

        // Soft delete the teacher
        teacher.setActive(false);
        teacherRepository.save(teacher);

        // Verify the teacher is soft-deleted
        Teacher deletedTeacher = teacherRepository.findById(teacher.getId()).orElse(null);
        assertNotNull(deletedTeacher);
        assertFalse(deletedTeacher.getActive());
    }

    @Test
    void testFindActiveUsersOnly() {
        // Create an active student
        Student student = new Student();
        student.setNome("Active Student");
        student.setEmail("active@test.com");
        student.setSenha("password123");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);
        studentRepository.save(student);

        // Create an inactive teacher
        Teacher teacher = new Teacher();
        teacher.setNome("Inactive Teacher");
        teacher.setEmail("inactive@test.com");
        teacher.setSenha("password123");
        teacher.setTipo(UserType.PROFESSOR);
        teacher.setActive(false);
        teacherRepository.save(teacher);

        // Get all active users - should include at least the active student
        var activeStudents = studentRepository.findByActiveTrue();
        var activeTeachers = teacherRepository.findByActiveTrue();
        
        assertTrue(activeStudents.size() >= 1);
        // Verify our active student is in the list
        assertTrue(activeStudents.stream().anyMatch(s -> "active@test.com".equals(s.getEmail())));
    }

    @Test
    void testFindUserById_Success() {
        Student student = new Student();
        student.setNome("Test Student");
        student.setEmail("test@test.com");
        student.setSenha("password123");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);
        student = studentRepository.save(student);

        var found = studentRepository.findById(student.getId());
        
        assertTrue(found.isPresent());
        assertEquals("test@test.com", found.get().getEmail());
    }

    @Test
    void testFindUserById_NotFound() {
        var found = studentRepository.findById(99999L);
        assertFalse(found.isPresent());
    }
}
