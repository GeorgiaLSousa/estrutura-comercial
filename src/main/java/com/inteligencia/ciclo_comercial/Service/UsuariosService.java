package com.inteligencia.ciclo_comercial.Service;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    public Usuario save(Usuario usuarios) {
        return usuariosRepository.save(usuarios);
    }

    public List<Usuario> findAll() {
        return usuariosRepository.findAll();
    }

    public List<Usuario> findByNome(String nome) {
        return usuariosRepository.findByNome(nome);
    }

    public Optional<Usuario> findByEmailUsuario(String emailUsuario) {
        return usuariosRepository.findByEmailUsuario(emailUsuario);
    }

    public Optional<Usuario> toggleUsuarioAtivo(String emailUsuario) {
        Optional<Usuario> usuariosOpt = usuariosRepository.findByEmailUsuario(emailUsuario);
        if (usuariosOpt.isPresent()) {
            Usuario usuarios = usuariosOpt.get();
            usuarios.setAtivo(!usuarios.getAtivo());
            usuariosRepository.save(usuarios);
        }
        return usuariosOpt;
    }

    public void toggleUsuarioAtivo(Usuario usuarios) {
        usuarios.setAtivo(!usuarios.getAtivo());
        usuariosRepository.save(usuarios);
    }
}