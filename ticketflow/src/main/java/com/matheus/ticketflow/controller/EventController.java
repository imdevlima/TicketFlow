package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Event;
import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.domain.Venue;
import com.matheus.ticketflow.dto.EventDTO;
import com.matheus.ticketflow.repository.EventRepository;
import com.matheus.ticketflow.repository.UserRepository;
import com.matheus.ticketflow.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List; // <--- Importante
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

    // --- A PARTE QUE FALTAVA (LISTAR) ---
    @GetMapping
    public ResponseEntity<List<Event>> listAll() {
        return ResponseEntity.ok(eventRepository.findAll());
    }
}