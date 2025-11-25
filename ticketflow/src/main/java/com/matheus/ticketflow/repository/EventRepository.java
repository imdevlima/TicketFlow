package com.matheus.ticketflow.repository;

import com.matheus.ticketflow.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // O Spring cria o SQL sozinho baseado no nome do método:
    // Busca pelo título, ignorando maiúsculas/minúsculas, com paginação.
    Page<Event> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}