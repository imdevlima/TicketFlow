package com.matheus.ticketflow.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    // --- CORREÇÃO AQUI ---
    // O banco chama 'criado_em'
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime dataCompra;
}