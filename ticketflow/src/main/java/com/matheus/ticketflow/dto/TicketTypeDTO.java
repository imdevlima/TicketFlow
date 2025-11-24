package com.matheus.ticketflow.dto;

import java.math.BigDecimal;

public record TicketTypeDTO(
        String nome,
        BigDecimal preco,
        Integer quantidadeTotal, // Mudamos o nome para ficar claro
        String eventId
) {}