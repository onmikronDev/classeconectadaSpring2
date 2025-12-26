package com.me.classeconectada.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Teacher extends User {
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private SchoolClass turma;
    
    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Subject> subjects;
}
