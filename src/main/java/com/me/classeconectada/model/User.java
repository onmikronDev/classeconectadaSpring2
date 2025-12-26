package com.me.classeconectada.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @JsonIgnore
    private String senha;
    
    @Column(unique = true)
    private String cpf;
    
    private String telefone;
    private String endereco;
    private String pai;
    private String mae;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType tipo;
}
