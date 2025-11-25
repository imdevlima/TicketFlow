package com.matheus.ticketflow.repository;

import com.matheus.ticketflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // O método TEM que ficar dentro das chaves da interface
    User findByEmail(String email);

}