package com.me.classeconectada.config;

import com.me.classeconectada.model.*;
import com.me.classeconectada.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DirectorRepository directorRepository;
    private final GradeRepository gradeRepository;
    private final ObservationRepository observationRepository;
    
    @Override
    public void run(String... args) {
        // Check if data already exists
        if (userRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }
        
        try {
            log.info("Initializing database with sample data...");
            initializeData();
            log.info("Database initialization completed successfully!");
            log.info("Login credentials: admin@email.com / 123456");
        } catch (Exception e) {
            log.warn("Could not initialize database with sample data: " + e.getMessage());
        }
    }
    
    private void initializeData() {
        // SECURITY NOTE: Default passwords are plain text for demo/educational purposes only
        // In a production environment, passwords should be hashed using BCrypt
        
        // Create School Classes
        SchoolClass turmaA = createSchoolClass("Turma A");
        SchoolClass turmaB = createSchoolClass("Turma B");
        SchoolClass turmaC = createSchoolClass("Turma C");
        
        log.info("Created {} school classes", schoolClassRepository.count());
        
        // Create Director
        Director admin = new Director();
        admin.setNome("Administrador");
        admin.setEmail("admin@email.com");
        admin.setSenha("123456");
        admin.setCpf("000.000.000-00");
        admin.setTelefone("(11) 99999-9999");
        admin.setTipo(UserType.DIRETOR);
        admin.setActive(true);
        admin.setSetor("Administração");
        directorRepository.save(admin);
        
        log.info("Created director: {}", admin.getEmail());
        
        // Create Teachers
        Teacher prof1 = createTeacher("João Silva", "joao@email.com", "123456", turmaA);
        Teacher prof2 = createTeacher("Ana Costa", "ana@email.com", "123456", turmaB);
        Teacher prof3 = createTeacher("Carlos Mendes", "carlos@email.com", "123456", turmaC);
        
        log.info("Created {} teachers", teacherRepository.count());
        
        // Create Subjects
        Subject matematica = createSubject("Matemática", prof1);
        Subject portugues = createSubject("Português", prof2);
        Subject ciencias = createSubject("Ciências", prof1);
        Subject geografia = createSubject("Geografia", prof3);
        Subject historia = createSubject("História", prof2);
        
        log.info("Created {} subjects", subjectRepository.count());
        
        // Create Students for Turma A
        Student alice = createStudent("Alice Santos", "alice@email.com", "111.111.111-11", turmaA);
        Student joao = createStudent("João Pedro", "joao.aluno@email.com", "222.222.222-22", turmaA);
        Student maria = createStudent("Maria Silva", "maria@email.com", "333.333.333-33", turmaA);
        
        // Create Students for Turma B
        Student pedro = createStudent("Pedro Lima", "pedro@email.com", "444.444.444-44", turmaB);
        Student julia = createStudent("Julia Oliveira", "julia@email.com", "555.555.555-55", turmaB);
        
        // Create Students for Turma C
        Student lucas = createStudent("Lucas Costa", "lucas@email.com", "666.666.666-66", turmaC);
        Student beatriz = createStudent("Beatriz Souza", "beatriz@email.com", "777.777.777-77", turmaC);
        
        log.info("Created {} students", studentRepository.count());
        
        // Create Grades for Alice
        createGrade(alice, matematica, 8.5, "Prova Bimestral");
        createGrade(alice, portugues, 9.0, "Redação");
        createGrade(alice, ciencias, 7.5, "Trabalho");
        
        // Create Grades for João
        createGrade(joao, matematica, 7.0, "Prova Bimestral");
        createGrade(joao, portugues, 8.0, "Redação");
        
        // Create Grades for Maria
        createGrade(maria, matematica, 9.5, "Prova Bimestral");
        createGrade(maria, ciencias, 9.0, "Trabalho");
        
        // Create Grades for Pedro
        createGrade(pedro, portugues, 8.5, "Redação");
        createGrade(pedro, historia, 7.5, "Prova");
        
        // Create Grades for Julia
        createGrade(julia, portugues, 9.5, "Redação");
        createGrade(julia, historia, 8.0, "Prova");
        
        log.info("Created {} grades", gradeRepository.count());
        
        // Create Observations
        createObservation(alice, turmaA, "Aluna exemplar, sempre participativa nas aulas.");
        createObservation(joao, turmaA, "Precisa melhorar a concentração durante as aulas.");
        createObservation(maria, turmaA, "Excelente desempenho em todas as matérias.");
        createObservation(pedro, turmaB, "Demonstra grande interesse por literatura.");
        
        log.info("Created {} observations", observationRepository.count());
    }
    
    private SchoolClass createSchoolClass(String name) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(name);
        schoolClass.setActive(true);
        return schoolClassRepository.save(schoolClass);
    }
    
    private Teacher createTeacher(String nome, String email, String senha, SchoolClass turma) {
        Teacher teacher = new Teacher();
        teacher.setNome(nome);
        teacher.setEmail(email);
        teacher.setSenha(senha);
        teacher.setCpf(generateCpf());
        teacher.setTelefone("(11) 98765-4321");
        teacher.setTipo(UserType.PROFESSOR);
        teacher.setActive(true);
        teacher.setTurma(turma);
        return teacherRepository.save(teacher);
    }
    
    private Subject createSubject(String name, Teacher teacher) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setTeacher(teacher);
        subject.setActive(true);
        return subjectRepository.save(subject);
    }
    
    private Student createStudent(String nome, String email, String cpf, SchoolClass turma) {
        Student student = new Student();
        student.setNome(nome);
        student.setEmail(email);
        student.setSenha("123456");
        student.setCpf(cpf);
        student.setTelefone("(11) 91234-5678");
        student.setTipo(UserType.ALUNO);
        student.setActive(true);
        student.setTurma(turma);
        student.setPai("Pai de " + nome.split(" ")[0]);
        student.setMae("Mãe de " + nome.split(" ")[0]);
        return studentRepository.save(student);
    }
    
    private void createGrade(Student student, Subject subject, Double value, String description) {
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setValue(value);
        grade.setDescription(description);
        grade.setExamDate(LocalDate.now());
        gradeRepository.save(grade);
    }
    
    private void createObservation(Student student, SchoolClass turma, String content) {
        Observation observation = new Observation();
        observation.setStudent(student);
        observation.setTurma(turma);
        observation.setContent(content);
        observation.setDate(LocalDate.now());
        observationRepository.save(observation);
    }
    
    private String generateCpf() {
        return String.format("%03d.%03d.%03d-%02d", 
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000),
            (int)(Math.random() * 100));
    }
}
