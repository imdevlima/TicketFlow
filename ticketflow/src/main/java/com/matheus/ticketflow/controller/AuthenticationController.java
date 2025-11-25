package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.dto.UserCreateDTO;
import com.matheus.ticketflow.infra.security.TokenService;
import com.matheus.ticketflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    // --- LOGIN (Entrar) ---
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserCreateDTO data){
        // 1. Encapsula email e senha pro Spring Security
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        // 2. O Spring vai no banco, checa o hash da senha e valida
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se deu certo, gera o Token JWT
        var token = tokenService.generateToken((User) auth.getPrincipal());

        // 4. Devolve o token pro usuário
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // --- REGISTER (Criar Conta) ---
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserCreateDTO data){
        // Verifica se já existe
        if(this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        // CRIPTOGRAFA A SENHA (Muito importante!)
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User();
        newUser.setNome(data.nome());
        newUser.setEmail(data.email());
        newUser.setPasswordHash(encryptedPassword); // Salva o hash, não a senha pura
        newUser.setPerfil(data.perfil());
        newUser.setCreatedAt(LocalDateTime.now());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}

// DTO simples para a resposta do login
record LoginResponseDTO(String token) {}