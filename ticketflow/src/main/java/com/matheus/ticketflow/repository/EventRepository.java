package com.matheus.ticketflow.repository;

import com.matheus.ticketflow.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    // Aqui no futuro podemos criar buscas como:
    // List<Event> findByStatus(String status);
}