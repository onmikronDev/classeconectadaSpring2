package com.me.classeconectada.repository;

import com.me.classeconectada.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByStudentId(Long studentId);
    List<Observation> findByTurmaId(Long turmaId);
}
