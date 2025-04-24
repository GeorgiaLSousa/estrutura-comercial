package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Service.RevisaoService;
import com.inteligencia.ciclo_comercial.Service.TerritorioService;
import com.inteligencia.ciclo_comercial.Service.UsuariosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/revisao")
public class RevisaoController {

    private static final Logger logger = LoggerFactory.getLogger(RevisaoController.class);


    @Autowired
    private TerritorioService territorioService;

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private RevisaoService revisaoService;

    @GetMapping("/detalhes-territorio")
    public String detalhesTerritorio(@RequestParam(value = "codigoTerritorio", required = false) String codigoTerritorio,
                                     @RequestParam(value = "codigoRegional", required = false) String codigoRegional,
                                     Model model) {
        Optional<Territorio> territorioOpt = Optional.empty();

        if (codigoTerritorio != null) {
            territorioOpt = revisaoService.findByCodigoTerritorio(codigoTerritorio);
        } else if (codigoRegional != null) {
            territorioOpt = revisaoService.findFirstByCodigoRegional(codigoRegional);
        }

        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            model.addAttribute("territorio", territorio);
            return "revisao-pessoas"; // Ensure this view exists
        } else {
            model.addAttribute("mensagem", "Território não encontrado!");
            return "redirect:/territorio/lista-territorio";
        }
    }

    @PostMapping("/atualizar-email")
    public String atualizarEmail(@RequestParam(value = "codigoTerritorio", required = false) String codigoTerritorio,
                                 @RequestParam(value = "codigoRegional", required = false) String codigoRegional,
                                 @RequestParam(value = "codigoFilial", required = false) String codigoFilial,
                                 @RequestParam("emailRTV") String emailRTV, Model model, Authentication authentication) {
        logger.info("Iniciando atualização de email para o território: {}", codigoTerritorio);

        if (!emailRTV.toLowerCase().endsWith("@syngenta.com")) {
            model.addAttribute("mensagem", "O email deve ser do domínio Syngenta.com");
            addTerritorioToModel(codigoTerritorio, codigoRegional, model);
            return "revisao-pessoas";
        }

        if (!revisaoService.validarEmailEAtivo(emailRTV)) {
            model.addAttribute("mensagem", "O Usuario deve ser ativo");
            addTerritorioToModel(codigoTerritorio, codigoRegional, model);
            return "revisao-pessoas";
        }

        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String nome = usuario.getUserNome();
            revisaoService.atualizarEmail(codigoTerritorio, codigoRegional, codigoFilial, emailRTV);
            revisaoService.atualizarModificadoPor(codigoTerritorio, codigoRegional, codigoFilial, nome);
            revisaoService.atualizarStatus(codigoTerritorio, "Realocado");
            model.addAttribute("mensagem", "Email atualizado com sucesso!");
            logger.info("Email atualizado com sucesso para o território: {}", codigoTerritorio);
            return "redirect:/territorio/lista-territorio";
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
            logger.warn("Usuário não encontrado para o email: {}", email);
        }

        addTerritorioToModel(codigoTerritorio, codigoRegional, model);
        return "revisao-pessoas";
    }

    private void addTerritorioToModel(String codigoTerritorio, String codigoRegional, Model model) {
        Optional<Territorio> territorioOpt = Optional.empty();

        if (codigoTerritorio != null) {
            territorioOpt = revisaoService.findByCodigoTerritorio(codigoTerritorio);
        } else if (codigoRegional != null) {
            territorioOpt = revisaoService.findFirstByCodigoRegional(codigoRegional);
        }

        if (territorioOpt.isPresent()) {
            model.addAttribute("territorio", territorioOpt.get());
        }
    }
}