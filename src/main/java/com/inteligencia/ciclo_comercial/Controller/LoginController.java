package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        SecurityContextHolder.clearContext();
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("emailUsuario") String emailUsuario, @RequestParam("senha") String senha, Model model) {
        String userEmail = emailUsuario.toLowerCase();
        Optional<Usuario> usuario = usuariosService.findByEmailUsuario(userEmail);

        if (usuario.isEmpty()) {
            model.addAttribute("error", "Email inválido!");
            return "login";
        } else if (!senha.equals(usuario.get().getSenha())) {
            model.addAttribute("error", "Senha incorreta!");
            return "login";
        } else if (!usuario.get().getAtivo()) {
            model.addAttribute("error", "Usuário inativo!");
            return "login";
        } else if (usuario.get().getGrupo().equals("CLIENTE")) {
            model.addAttribute("error", "Acesso negado!");
            return "login";
        } else if ("A1B2C3".equals(usuario.get().getSenha())) {
            return "redirect:/redefinir-senha";
        } else {
            try {
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userEmail, senha)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                return "redirect:/dashboard";
            } catch (AuthenticationException e) {
                model.addAttribute("error", "Erro de autenticação!");
                return "login";
            }
        }
    }

    @GetMapping("/redefinir-senha")
    public String showRedefinirSenhaPage(Model model) {
        // Add the 'usuarios' attribute to the model
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuarios", usuarioOpt.get());
        }
        return "redefinir-senha";
    }

 @PostMapping("/redefinir-senha")
public String editarUsuario(@RequestParam("emailUsuario") String emailUsuario, @RequestParam("senha") String senha, @RequestParam("grupo") String grupo, Model model) {
    Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(emailUsuario);
    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(senha);
        usuario.setGrupo(grupo); // Mantém o grupo do usuário
        usuariosService.save(usuario);
        model.addAttribute("mensagem", "Usuário editado com sucesso!");
    } else {
        model.addAttribute("mensagem", "Usuário não encontrado!");
    }
    return "redirect:/territorio/lista-territorio";
}
}