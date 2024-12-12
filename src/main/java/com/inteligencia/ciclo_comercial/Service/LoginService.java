package com.inteligencia.ciclo_comercial.Service;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String userEmail = email.toLowerCase();

        Optional<Usuario> usuarioOpt = usuariosRepository.findByEmailUsuario(userEmail);

        if (!usuarioOpt.isPresent()) {
            System.out.println("Usuário não encontrado: " + userEmail);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario != null) {
            if (!usuario.getAtivo()) {
                System.out.println("Usuário desativado: " + userEmail);
                throw new UsernameNotFoundException("Usuário desativado");
            }
            return new User(usuario.getEmailUsuario(), usuario.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getGrupo())));
        }
        return null;
    }

}