package com.inteligencia.ciclo_comercial.Config;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Repository.UsuariosRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = request.getContextPath();

        // Retrieve the authenticated user's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String emailUsuario = userDetails.getUsername();

        // Retrieve the user from the database
        Optional<Usuario> usuarioOpt = usuariosRepository.findByEmailUsuario(emailUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String password = usuario.getSenha();

            // Check if the password is "A1B2C3"
            if ("A1B2C3".equals(password)) {
                redirectUrl += "/redefinir-senha";
            } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ANALISTA"))) {
                redirectUrl += "/territorio/lista-territorio";
            } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
                redirectUrl += "/dashboard";
            }
        }

        response.sendRedirect(redirectUrl);
    }
}