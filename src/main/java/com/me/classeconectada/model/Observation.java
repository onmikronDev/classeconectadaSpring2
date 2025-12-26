package com.me.classeconectada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "observations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Aluno é obrigatório")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private SchoolClass turma;
    
    @Column(length = 1000)
    @NotBlank(message = "Conteúdo da observação é obrigatório")
    private String content;
    
    private LocalDate date;
}
