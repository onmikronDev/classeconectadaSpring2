package com.me.classeconectada.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    private Long id;
    
    @NotNull(message = "ID do aluno é obrigatório")
    private Long studentId;
    
    private String studentNome;
    
    @NotNull(message = "ID da matéria é obrigatório")
    private Long subjectId;
    
    private String subjectNome;
    
    @NotNull(message = "Nota é obrigatória")
    @Min(value = 0, message = "Nota mínima é 0")
    @Max(value = 10, message = "Nota máxima é 10")
    private Double value;
    
    private String description;
    private String examDate; // ✅ CORRIGIDO: String para aceitar formato "yyyy-MM-dd" do frontend
}
