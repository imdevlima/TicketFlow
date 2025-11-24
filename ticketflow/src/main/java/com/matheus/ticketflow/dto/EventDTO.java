package com.matheus.ticketflow.dto;

public record EventDTO(
        String titulo,
        String descricao,
        String data, // Vamos receber como texto: "2025-12-25T20:00:00"
        String localId,      // O ID do Venue (Allianz Parque)
        String organizadorId // O ID do User (Você)
) {}