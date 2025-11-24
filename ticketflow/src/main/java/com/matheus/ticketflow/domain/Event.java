package com.matheus.ticketflow.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "eventos") // Tabela 'eventos' no banco
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT") // Permite textos grandes na descrição
    private String descricao;

    @Column(name = "data_evento", nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status; // Ex: "PUBLICADO", "RASCUNHO"

    // --- RELACIONAMENTOS ---

    // Muitos eventos podem acontecer no mesmo Local
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Venue venue;

    // Muitos eventos podem ser criados pelo mesmo Organizador
    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private User organizer;

    @Column(name = "criado_em")
    private LocalDateTime createdAt;
}