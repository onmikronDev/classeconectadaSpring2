package com.me.classeconectada.dto;

import com.me.classeconectada.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String endereco;
    private String pai;
    private String mae;
    private Boolean active;
    private UserType tipo;
    private Long turmaId;
    private String turmaNome;
}
