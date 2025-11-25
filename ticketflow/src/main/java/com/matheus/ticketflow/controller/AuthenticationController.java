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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserCreateDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        User user = (User) auth.getPrincipal(); // Pega o usuário logado
        var token = tokenService.generateToken(user);

        // Agora devolvemos o Token E o ID dele
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getId().toString(), user.getNome()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserCreateDTO data){
        if(this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User();
        newUser.setNome(data.nome());
        newUser.setEmail(data.email());
        newUser.setPasswordHash(encryptedPassword);
        newUser.setPerfil(data.perfil());
        newUser.setCreatedAt(LocalDateTime.now());
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}

// DTO atualizado com mais dados
record LoginResponseDTO(String token, String userId, String nome) {}