package com.matheus.ticketflow.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.matheus.ticketflow.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Essa é a "chave secreta" para criptografar o token.
    // Na vida real, fica em variável de ambiente.
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ticketflow-api") // Quem emitiu
                    .withSubject(user.getEmail()) // Quem é o dono do token
                    .withExpiresAt(genExpirationDate()) // Quando expira
                    .sign(algorithm); // Assina
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ticketflow-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o email se o token for válido
        } catch (JWTVerificationException exception) {
            return ""; // Retorna vazio se for inválido
        }
    }

    private Instant genExpirationDate() {
        // Expira em 2 horas (fuso horário de Brasília -3)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}