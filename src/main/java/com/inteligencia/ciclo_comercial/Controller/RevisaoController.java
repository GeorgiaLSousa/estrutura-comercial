package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Service.RevisaoService;
import com.inteligencia.ciclo_comercial.Service.TerritorioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/revisao")
public class RevisaoController {

    @Autowired
    private TerritorioService territorioService;

    @Autowired
    private RevisaoService revisaoService;

    @GetMapping("/detalhes-territorio/{codigoTerritorio}")
    public String detalhesTerritorio(@PathVariable("codigoTerritorio") String codigoTerritorio, Model model) {
        Optional<Territorio> territorioOpt = revisaoService.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            model.addAttribute("territorio", territorio);
            return "revisao-pessoas";
        } else {
            model.addAttribute("mensagem", "Território não encontrado!");
            return "redirect:/territorio/lista-territorio";
        }
    }

    @PostMapping("/atualizar-email")
    public String atualizarEmail(@RequestParam("codigoTerritorio") String codigoTerritorio, @RequestParam("emailRTV") String emailRTV, Model model) {
        revisaoService.atualizarEmail(codigoTerritorio, emailRTV);
        model.addAttribute("mensagem", "Email atualizado com sucesso!");
        return "redirect:/territorio/lista-territorio";
    }
}
