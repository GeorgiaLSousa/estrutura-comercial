package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Service.LoginService;
import com.inteligencia.ciclo_comercial.Service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/cadastro-usuario")
    public String showForm(Model model){
        model.addAttribute("usuarios", new Usuario());
        return "cadastro-usuario";
    }

    @PostMapping("/cadastro-usuario")
    public String registerUsuario(@ModelAttribute Usuario usuarios, Model model) {
        usuarios.setEmailUsuario(usuarios.getEmailUsuario().toLowerCase());

        try {
            Optional<Usuario> usuarioExistente = usuariosService.findByEmailUsuario(usuarios.getEmailUsuario());
            if (usuarioExistente.isPresent()) {
                model.addAttribute("mensagem", "Usuário já registrado!");
                return "cadastro-usuario";
            }

            usuarios.setAtivo(true); // Define o campo ativo como true
            Usuario usuarioSalvo = usuariosService.save(usuarios);

            model.addAttribute("mensagem", "Usuário registrado com sucesso!");
            return "redirect:/usuarios/lista-usuarios"; // Redireciona para a lista de usuários

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("mensagem", "Erro: Usuário já registrado!");
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao registrar usuário!");
        }
        return "cadastro-usuario";
    }

    @GetMapping("/editar-usuario/{emailUsuario}")
    public String editarUsuario(@PathVariable("emailUsuario") String emailUsuario, Model model) {
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(emailUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuarios", usuario);
            return "editar-usuario";
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
            return "redirect:/usuarios/lista-usuarios";
        }
    }

    @PostMapping("/editar-usuario")
    public String editarUsuario(@RequestParam("emailUsuario") String emailUsuario, @RequestParam("senha") String senha, @RequestParam("grupo") String grupo, Model model) {
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(emailUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSenha(senha);
            usuario.setGrupo(grupo);
            usuariosService.save(usuario);
            model.addAttribute("mensagem", "Usuário editado com sucesso!");
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
        }
        return "redirect:/usuarios/lista-usuarios";
    }

    @GetMapping("/lista-usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuariosService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "lista-usuarios";
    }

    @GetMapping("/buscar-usuario")
    public String buscarUsuario(@RequestParam("nome") String nome, Model model) {
        List<Usuario> usuarios = usuariosService.findByNome(nome);
        model.addAttribute("usuarios", usuarios);
        return "fragments/tabela-usuarios :: tabela-usuarios";
    }

    @GetMapping("/alterar-estado-usuario")
    public String alterarEstadoUsuario(@RequestParam("emailUsuario") String emailUsuario, Model model) {
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(emailUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setAtivo(!usuario.getAtivo());
            usuariosService.save(usuario);
            model.addAttribute("mensagem", "Estado do usuário alterado com sucesso!");
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
        }
        return "redirect:/usuarios/lista-usuarios";
    }

    @PostMapping("/alterar-estado-usuario")
    public String alterarEstadoUsuario(@ModelAttribute Usuario usuarios, Model model) {
        usuariosService.toggleUsuarioAtivo(usuarios);
        return "redirect:/usuarios/lista-usuarios";
    }

    @PostMapping
    public String atualizarListaUsuarios(Model model) {
        List<Usuario> usuarios = usuariosService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "fragments/tabela-usuarios :: tabela-usuarios";
    }



}