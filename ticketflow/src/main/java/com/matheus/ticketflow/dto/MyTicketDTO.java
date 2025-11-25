package com.matheus.ticketflow.dto;

import java.util.UUID;

public record MyTicketDTO(
        UUID id,
        String eventoTitulo,
        String eventoData,
        String localNome,
        String tipoIngresso, // Ex: Pista Premium
        String codigoQr      // O hash que vamos transformar em imagem
) {}