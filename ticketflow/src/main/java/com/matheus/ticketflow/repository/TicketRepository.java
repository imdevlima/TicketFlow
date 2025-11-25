package com.matheus.ticketflow.repository;

import com.matheus.ticketflow.domain.Ticket;
import com.matheus.ticketflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByOrder_User(User user);
}