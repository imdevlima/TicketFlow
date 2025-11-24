package com.matheus.ticketflow.dto;

public record UserCreateDTO(
        String nome,
        String email,
        String password,
        String perfil
) {}