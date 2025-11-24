package com.matheus.ticketflow.dto;

public record OrderDTO(
        String userId,         // Quem está comprando
        String ticketTypeId,   // Qual ingresso (Pista Premium)
        Integer quantidade     // Quantos (ex: 2)
) {}