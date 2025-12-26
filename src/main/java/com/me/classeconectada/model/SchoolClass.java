package com.me.classeconectada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "school_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome da turma é obrigatório")
    private String name;
    
    @OneToMany(mappedBy = "turma")
    @JsonIgnore
    private List<Student> students;
    
    @OneToMany(mappedBy = "turma")
    @JsonIgnore
    private List<Teacher> teachers;
    
    @Column(nullable = false)
    private Boolean active = true;
}
