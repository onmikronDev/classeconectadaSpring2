package com.me.classeconectada.controller;

import com.me.classeconectada.dto.LoginDTO;
import com.me.classeconectada.model.User;
import com.me.classeconectada.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.authenticate(loginDTO.getEmail(), loginDTO.getSenha());
        
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login realizado com sucesso");
            
            // Return user data without password
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("nome", user.getNome());
            userData.put("email", user.getEmail());
            userData.put("cpf", user.getCpf());
            userData.put("telefone", user.getTelefone());
            userData.put("tipo", user.getTipo());
            userData.put("active", user.getActive());
            
            response.put("user", userData);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Email ou senha incorretos");
            return ResponseEntity.status(401).body(response);
        }
    }
}
