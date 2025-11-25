package com.matheus.ticketflow.infra.security;

import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("=== [DEBUG] 1. O Filtro de Segurança foi chamado! ===");

        var token = this.recoverToken(request);
        System.out.println("=== [DEBUG] 2. Token recuperado do cabeçalho: " + token);

        if(token != null){
            try {
                var email = tokenService.validateToken(token);
                System.out.println("=== [DEBUG] 3. Email extraído do Token: " + email);

                if(email != null && !email.isEmpty()){
                    User user = userRepository.findByEmail(email);

                    if(user != null) {
                        System.out.println("=== [DEBUG] 4. Usuário encontrado no banco: " + user.getEmail() + " (Perfil: " + user.getPerfil() + ")");
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("=== [DEBUG] 5. Autenticação forçada no Contexto do Spring! ===");
                    } else {
                        System.out.println("=== [DEBUG] ERRO: Token válido, mas usuário não existe no banco! ===");
                    }
                } else {
                    System.out.println("=== [DEBUG] ERRO: O Token é inválido ou expirado (Email vazio). ===");
                }
            } catch (Exception e) {
                System.out.println("=== [DEBUG] ERRO CRÍTICO NA VALIDAÇÃO: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("=== [DEBUG] AVISO: Nenhum token foi enviado na requisição. ===");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}