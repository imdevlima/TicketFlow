package com.matheus.ticketflow.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios") // Liga com a tabela 'usuarios' do PostgreSQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false) // Mapeia 'senha_hash' do banco
    private String passwordHash;

    @Column(nullable = false)
    private String perfil; // Vamos salvar como "COMPRADOR" ou "ADMIN"

    @Column(name = "criado_em") // Mapeia 'criado_em' do banco
    private LocalDateTime createdAt;
}