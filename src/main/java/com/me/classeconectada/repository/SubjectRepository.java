package com.me.classeconectada.repository;

import com.me.classeconectada.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByActiveTrue();
    List<Subject> findByTeacherId(Long teacherId);
}
