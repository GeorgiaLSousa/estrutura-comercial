package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Service.MovimentacaoService;
import com.inteligencia.ciclo_comercial.Service.TerritorioService;
import com.inteligencia.ciclo_comercial.Service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    @Autowired
    private TerritorioService territorioService;

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping("/movimentacao-regional/{codigoTerritorio}")
    public String detalhesTerritorio(@PathVariable("codigoTerritorio") String codigoTerritorio, Model model) {
        Optional<Territorio> territorioOpt = movimentacaoService.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            model.addAttribute("territorio", territorio);
            return "movimentacao";
        } else {
            model.addAttribute("mensagem", "Território não encontrado!");
            return "redirect:/territorio/lista-territorio";
        }
    }

    @PostMapping("/atualizar-regional")
    public String atualizarRegional(@RequestParam("codigoTerritorio") String codigoTerritorio,
                                    @RequestParam("codigoRegional") String codigoRegional,
                                    @RequestParam("codigoFilial") String codigoFilial,
                                    @RequestParam("nomeUnidade") String nomeUnidade,
                                    Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String nome = usuario.getUserNome();
            movimentacaoService.atualizarRegional(codigoTerritorio, codigoRegional, codigoFilial, nomeUnidade);
            movimentacaoService.atualizarModificadoPor(codigoTerritorio, codigoRegional, codigoFilial, nome);
            movimentacaoService.atualizarStatus(codigoTerritorio, "Movimentado");
            model.addAttribute("mensagem", "Dados atualizados com sucesso!");
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
        }
        return "redirect:/territorio/lista-territorio";
    }
}