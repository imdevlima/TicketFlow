package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Venue;
import com.matheus.ticketflow.dto.VenueDTO;
import com.matheus.ticketflow.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueRepository venueRepository;

    @PostMapping
    public ResponseEntity<Venue> create(@RequestBody VenueDTO data) {
        Venue newVenue = new Venue();
        newVenue.setNome(data.nome());
        newVenue.setEndereco(data.endereco());
        newVenue.setCapacidade(data.capacidade());
        Venue savedVenue = venueRepository.save(newVenue);
        return ResponseEntity.ok(savedVenue);
    }

    // --- NOVO MÉTODO PARA LISTAR ---
    @GetMapping
    public ResponseEntity<List<Venue>> listAll() {
        return ResponseEntity.ok(venueRepository.findAll());
    }
}