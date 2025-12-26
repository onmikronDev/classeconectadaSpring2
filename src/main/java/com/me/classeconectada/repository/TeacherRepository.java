package com.me.classeconectada.repository;

import com.me.classeconectada.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByTurmaId(Long turmaId);
    List<Teacher> findByActiveTrue();
}
