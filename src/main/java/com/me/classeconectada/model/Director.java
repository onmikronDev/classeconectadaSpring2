package com.me.classeconectada.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "directors")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Director extends User {
    private String setor;
}
