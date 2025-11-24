package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Event;
import com.matheus.ticketflow.domain.TicketType;
import com.matheus.ticketflow.dto.TicketTypeDTO;
import com.matheus.ticketflow.repository.EventRepository;
import com.matheus.ticketflow.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ticket-types")
public class TicketTypeController {

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<TicketType> create(@RequestBody TicketTypeDTO data) {
        Event event = eventRepository.findById(UUID.fromString(data.eventId()))
                .orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

        TicketType newType = new TicketType();
        newType.setNome(data.nome());
        newType.setPreco(data.preco());

        // --- CORREÇÃO AQUI ---
        newType.setQuantidadeTotal(data.quantidadeTotal());
        // No começo, o disponível é igual ao total (ninguém comprou ainda)
        newType.setQuantidadeDisponivel(data.quantidadeTotal());

        newType.setEvent(event);

        ticketTypeRepository.save(newType);

        return ResponseEntity.ok(newType);
    }

    @GetMapping
    public ResponseEntity<List<TicketType>> listAll() {
        return ResponseEntity.ok(ticketTypeRepository.findAll());
    }
}