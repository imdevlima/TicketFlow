package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Ticket;
import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.dto.MyTicketDTO;
import com.matheus.ticketflow.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/my-tickets")
    public ResponseEntity<List<MyTicketDTO>> getMyTickets() {
        // 1. Descobrir quem está logado (Magia do Spring Security)
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Buscar ingressos desse usuário
        List<Ticket> tickets = ticketRepository.findByOrder_User(user);

        // 3. Converter para DTO (Resumo Bonito)
        List<MyTicketDTO> dtos = tickets.stream().map(ticket -> new MyTicketDTO(
                ticket.getId(),
                ticket.getTicketType().getEvent().getTitulo(),
                ticket.getTicketType().getEvent().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                ticket.getTicketType().getEvent().getVenue().getNome(),
                ticket.getTicketType().getNome(),
                ticket.getCodigoQr()
        )).toList();

        return ResponseEntity.ok(dtos);
    }
}