package com.matheus.ticketflow.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "ingressos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "tipo_ingresso_id", nullable = false)
    private TicketType ticketType;

    // --- CORREÇÃO AQUI ---
    // O banco chama 'codigo_qr_hash', o Java chama 'codigoQr'
    @Column(name = "codigo_qr_hash", nullable = false)
    private String codigoQr;
}