package com.me.classeconectada.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Student extends User {
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private SchoolClass turma;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Grade> grades;
    
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Observation> observations;
}
