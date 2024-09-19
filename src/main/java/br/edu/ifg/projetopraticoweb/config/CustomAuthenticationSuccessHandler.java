package br.edu.ifg.projetopraticoweb.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Pegue as roles do usuário autenticado
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/task/list"; // Default para usuários com ROLE_USER

        // Checa as roles do usuário e ajusta a URL de redirecionamento
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_SUPERVISOR")) {
                redirectUrl = "/project/list";
                break;
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/user/list";
                break;
            }
        }

        // Redireciona para a URL correta
        response.sendRedirect(redirectUrl);
    }
}

