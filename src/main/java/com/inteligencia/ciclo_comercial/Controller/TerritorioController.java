package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
import com.inteligencia.ciclo_comercial.Service.TerritorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/territorio")
public class TerritorioController {

    @Autowired
    private TerritorioService territorioService;

    @Autowired
    private TerritorioRepository territorioRepository;

    @GetMapping("/cadastro-territorio")
    public String showForm(Model model){
        model.addAttribute("territorio", new Territorio());
        return "novo-territorio";
    }

   @PostMapping("/cadastro-territorio")
public String registerTerritorio(@ModelAttribute Territorio territorio, Model model) {
    try {
        List<Territorio> territoriosExistentes = territorioService.findByNome(territorio.getCodigoTerritorio());
        if (!territoriosExistentes.isEmpty()) {
            model.addAttribute("mensagem", "Território já registrado!");
            return "novo-territorio";
        }

        territorio.setAtivo(true); // Define o campo ativo como true
        Territorio territorioSalvo = territorioService.save(territorio);

        model.addAttribute("mensagem", "Território registrado com sucesso!");
        return "redirect:/territorio/lista-territorio"; // Redireciona para a lista de territórios

    } catch (DataIntegrityViolationException e) {
        model.addAttribute("mensagem", "Erro: Território já registrado!");
    } catch (Exception e) {
        model.addAttribute("mensagem", "Erro ao registrar território!");
    }
    return "novo-territorio";
}
    @GetMapping("/lista-territorio")
    public String listarTerritorios(Model model) {
        List<Territorio> territorios = territorioService.findAll();
        model.addAttribute("territorios", territorios);
        return "lista-territorio";
    }

    @GetMapping("/buscar-territorio")
    public String buscarTerritorio(@RequestParam("codigoTerritorio") String codigoTerritorio, Model model) {
        List<Territorio> territorios = territorioService.findByNome(codigoTerritorio);
        model.addAttribute("territorios", territorios);
        return "fragments/tabela-territorio :: tabela-territorio";
    }

    @GetMapping("/alterar-estado-territorio")
    public String alterarEstadoTerritorio(@RequestParam("codigoTerritorio") String codigoTerritorio, Model model) {
        Optional<Territorio> territorioOpt = territorioService.alterar_estado_territorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            model.addAttribute("mensagem", "Estado do território alterado com sucesso!");
        } else {
            model.addAttribute("mensagem", "Território não encontrado!");
            return "redirect:/territorio/lista-territorio";
        }
        return atualizarListaTerritorios(model);
    }

    @PostMapping("/alterar-estado-territorio")
    public String alterarEstadoTerritorio(Territorio territorio, Model model) {
        territorioService.alterar_estado_territorio(territorio);
        model.addAttribute("mensagem", "Estado do território alterado com sucesso!");
        return atualizarListaTerritorios(model);
    }

    @GetMapping("/detalhes/{codigoTerritorio}")
    public String detalhesPedido(@PathVariable String codigoTerritorio, Model model) {
        List<Territorio> territorios = territorioService.findByNome(codigoTerritorio);

        if (!territorios.isEmpty()) {
          model.addAttribute("territorio", territorios.get(0));
          return "detalhe-territorio";
        }

        return null;
    }

    private String atualizarListaTerritorios(Model model) {
        List<Territorio> todosterritorios = territorioService.findAll();
        model.addAttribute("territorios", todosterritorios);
        return "lista-territorio";
    }
}