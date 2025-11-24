package com.matheus.ticketflow.dto;

public record VenueDTO(
        String nome,
        String endereco,
        Integer capacidade
) {}