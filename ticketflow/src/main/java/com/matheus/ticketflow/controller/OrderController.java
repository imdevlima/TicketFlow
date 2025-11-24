package com.matheus.ticketflow.controller;

import com.matheus.ticketflow.domain.Order;
import com.matheus.ticketflow.domain.Ticket;
import com.matheus.ticketflow.domain.TicketType;
import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.dto.OrderDTO;
import com.matheus.ticketflow.repository.OrderRepository;
import com.matheus.ticketflow.repository.TicketRepository;
import com.matheus.ticketflow.repository.TicketTypeRepository;
import com.matheus.ticketflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Importante!
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TicketTypeRepository ticketTypeRepository;

    @PostMapping
    @Transactional // Se der erro no meio, ele desfaz tudo (não cobra e não baixa estoque)
    public ResponseEntity<Order> create(@RequestBody OrderDTO data) {

        // 1. Achar o Comprador
        User user = userRepository.findById(UUID.fromString(data.userId()))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Achar o Produto (Lote de Ingressos)
        TicketType ticketType = ticketTypeRepository.findById(UUID.fromString(data.ticketTypeId()))
                .orElseThrow(() -> new RuntimeException("Tipo de ingresso não encontrado"));

        // 3. VALIDAR ESTOQUE (A regra de ouro)
        if (ticketType.getQuantidadeDisponivel() < data.quantidade()) {
            throw new RuntimeException("Eita! Ingressos esgotados ou insuficientes.");
        }

        // 4. Calcular preço total
        BigDecimal total = ticketType.getPreco().multiply(BigDecimal.valueOf(data.quantidade()));

        // 5. BAIXAR O ESTOQUE
        ticketType.setQuantidadeDisponivel(ticketType.getQuantidadeDisponivel() - data.quantidade());
        ticketTypeRepository.save(ticketType);

        // 6. Criar o Pedido
        Order order = new Order();
        order.setUser(user);
        order.setValorTotal(total);
        order.setDataCompra(LocalDateTime.now());
        orderRepository.save(order);

        // 7. Gerar os Ingressos Individuais (Loop)
        for (int i = 0; i < data.quantidade(); i++) {
            Ticket ticket = new Ticket();
            ticket.setOrder(order);
            ticket.setTicketType(ticketType);
            // Gerando um QR Code fake (UUID + data)
            ticket.setCodigoQr(UUID.randomUUID().toString());
            ticketRepository.save(ticket);
        }

        return ResponseEntity.ok(order);
    }
}