package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Service.MovimentacaoService;
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
@RequestMapping("/movimentacao")
public class MovimentacaoController {

    @Autowired
    private TerritorioService territorioService;

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
                                Model model) {
        movimentacaoService.atualizarRegional(codigoTerritorio, codigoRegional);
        model.addAttribute("mensagem", "Codigo Regional atualizado com sucesso!");
        return "redirect:/territorio/lista-territorio";
    }
}
