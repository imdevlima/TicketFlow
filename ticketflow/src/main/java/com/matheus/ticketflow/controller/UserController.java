package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.dto.UserCreateDTO;
import com.matheus.ticketflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserCreateDTO userDTO) {
        User newUser = new User();
        newUser.setNome(userDTO.nome());
        newUser.setEmail(userDTO.email());
        newUser.setPasswordHash(userDTO.password());
        newUser.setPerfil(userDTO.perfil());
        newUser.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    // --- NOVO MÉTODO PARA LISTAR ---
    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}