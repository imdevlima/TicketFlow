package com.matheus.ticketflow.infra.security;

import com.matheus.ticketflow.domain.User;
import com.matheus.ticketflow.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. Pegar dados do Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 2. Verificar se usuário existe no banco, senão CRIA
        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setNome(name);
            user.setEmail(email);
            user.setPasswordHash("GOOGLE_AUTH"); // Senha dummy, pois ele loga pelo Google
            user.setPerfil("COMPRADOR"); // Padrão
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }

        // 3. Gerar o Token JWT do NOSSO sistema
        String token = tokenService.generateToken(user);
        String userId = user.getId().toString();
        String userName = user.getNome();

        // 4. Redirecionar para o Front-end passando o token na URL
        // Atenção: Se seu front estiver rodando em outra porta (ex: 5500), ajuste aqui
        String targetUrl = "http://127.0.0.1:5500/index.html?token=" + token + "&userId=" + userId + "&name=" + userName;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}