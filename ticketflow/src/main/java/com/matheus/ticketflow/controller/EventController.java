package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Event;
import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.domain.Venue;
import com.matheus.ticketflow.dto.EventDTO;
import com.matheus.ticketflow.repository.EventRepository;
import com.matheus.ticketflow.repository.UserRepository;
import com.matheus.ticketflow.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    // --- 1. CRIAR (POST) ---
    @PostMapping
    public ResponseEntity<Event> create(@RequestBody EventDTO data) {
        Venue venue = venueRepository.findById(UUID.fromString(data.localId()))
                .orElseThrow(() -> new RuntimeException("Local não encontrado!"));

        User organizer = userRepository.findById(UUID.fromString(data.organizadorId()))
                .orElseThrow(() -> new RuntimeException("Organizador não encontrado!"));

        Event newEvent = new Event();
        newEvent.setTitulo(data.titulo());
        newEvent.setDescricao(data.descricao());
        newEvent.setDate(LocalDateTime.parse(data.data()));
        newEvent.setVenue(venue);
        newEvent.setOrganizer(organizer);
        newEvent.setStatus("PUBLICADO");
        newEvent.setCreatedAt(LocalDateTime.now());

        eventRepository.save(newEvent);

        return ResponseEntity.ok(newEvent);
    }

    // --- 2. LISTAR COM PAGINAÇÃO E BUSCA (GET) ---
    @GetMapping
    public ResponseEntity<Page<Event>> listAll(
            @PageableDefault(size = 10, sort = "date") Pageable pageable,
            @RequestParam(required = false) String query
    ) {
        if (query != null && !query.isEmpty()) {
            // Se tem busca, filtra pelo título
            return ResponseEntity.ok(eventRepository.findByTituloContainingIgnoreCase(query, pageable));
        }
        // Se não, traz tudo paginado
        return ResponseEntity.ok(eventRepository.findAll(pageable));
    }

    // --- 3. ATUALIZAR (PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable UUID id, @RequestBody EventDTO data) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        event.setTitulo(data.titulo());
        event.setDescricao(data.descricao());
        event.setDate(LocalDateTime.parse(data.data()));

        // Nota: Não estamos atualizando Local e Organizador aqui para simplificar,
        // mas você poderia buscar os novos IDs se quisesse mudar o local do show.

        eventRepository.save(event);
        return ResponseEntity.ok(event);
    }

    // --- 4. DELETAR (DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}