package com.me.classeconectada.repository;

import com.me.classeconectada.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByTurmaId(Long turmaId);
    List<Student> findByActiveTrue();
}
