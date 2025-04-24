package com.inteligencia.ciclo_comercial.Controller;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Model.Usuario;
import com.inteligencia.ciclo_comercial.Service.TerritorioService;
import com.inteligencia.ciclo_comercial.Service.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/territorio")
public class TerritorioController {

    @Autowired
    private TerritorioService territorioService;

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/cadastro-territorio")
    public String showForm(Model model) {
        model.addAttribute("territorio", new Territorio());
        return "cadastro-territorio";
    }

   @PostMapping("/cadastro-territorio")
public String registerTerritorio(@ModelAttribute @Valid Territorio territorio, BindingResult result, Model model, Authentication authentication) {
    if (result.hasErrors()) {
        model.addAttribute("mensagem", "Erro: " + result.getFieldError().getDefaultMessage());
        return "cadastro-territorio";
    }

    try {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String username = usuario.getUserNome();

            System.out.println("Nome da Unidade: " + territorio.getNomeUnidade()); // Log the nomeUnidade

            if (territorio.getCodigoTerritorio() != null && !territorio.getCodigoTerritorio().isEmpty()) {
                Optional<Territorio> territorioExistente = territorioService.findByCodigoTerritorio(territorio.getCodigoTerritorio());
                if (territorioExistente.isPresent()) {
                    model.addAttribute("mensagem", "Território já registrado!");
                    return "cadastro-territorio";
                }
            }

            Optional<Territorio> unidadeExistente = territorioService.findFirstByNomeUnidade(territorio.getNomeUnidade());
            if (unidadeExistente.isEmpty()) {
                System.out.println("Unidade não encontrada: " + territorio.getNomeUnidade());
                model.addAttribute("mensagem", "Unidade não encontrada!");
                return "cadastro-territorio";
            }

            territorio.setAtivo(true); // Define o campo ativo como true
            territorio.setStatus("Criado"); // Define o status como "Criado"
            territorio.setModificadoPor(username);
            Territorio territorioSalvo = territorioService.save(territorio, username);

            model.addAttribute("mensagem", "Território registrado com sucesso!");
            return "redirect:/territorio/lista-territorio"; // Redireciona para a lista de territórios
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
            return "cadastro-territorio";
        }
    } catch (DataIntegrityViolationException e) {
        model.addAttribute("mensagem", "Erro: Território já registrado!");
    } catch (Exception e) {
        model.addAttribute("mensagem", "Erro ao registrar território!");
    }
    return "cadastro-territorio";
}

    @GetMapping("/fetch")
    public ResponseEntity<Territorio> fetchTerritorio(@RequestParam(required = false) String codigoRegional, @RequestParam(required = false) String codigoFilial) {
        if (codigoRegional != null && !codigoRegional.isEmpty()) {
            Optional<Territorio> territorio = territorioService.findFirstByCodigoRegional(codigoRegional.toLowerCase());
            return territorio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } else if (codigoFilial != null && !codigoFilial.isEmpty()) {
            Optional<Territorio> territorio = territorioService.findFirstByCodigoFilial(codigoFilial.toLowerCase());
            return territorio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/fetchByNomeUnidade")
    public ResponseEntity<Boolean> fetchByNomeUnidade(@RequestParam String nomeUnidade) {
        Optional<Territorio> territorio = territorioService.findFirstByNomeUnidade(nomeUnidade.toLowerCase());
        return ResponseEntity.ok(territorio.isPresent());
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateUnidade(@RequestParam("nomeUnidade") String nomeUnidade) {
        boolean exists = territorioService.existsByNomeUnidade(nomeUnidade.toLowerCase());
        return ResponseEntity.ok(exists);
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

    @GetMapping("/buscar-filial")
    public String buscarFilial(@RequestParam("codigoFilial") String codigoFilial, Model model) {
        List<Territorio> territorios = territorioService.findByCodigoFilial(codigoFilial);
        model.addAttribute("territorios", territorios);
        return "fragments/tabela-territorio :: tabela-territorio";
    }

    @GetMapping("/buscar-unidade")
    public String buscarUnidade(@RequestParam("nomeUnidade") String nomeUnidade, Model model) {
        List<Territorio> territorios = territorioService.findByNomeUnidade(nomeUnidade);
        model.addAttribute("territorios", territorios);
        return "fragments/tabela-territorio :: tabela-territorio";
    }

    @GetMapping("/alterar-estado-territorio")
    public String alterarEstadoTerritorio(@RequestParam("codigoTerritorio") String codigoTerritorio, Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String nome = usuario.getUserNome();
            Optional<Territorio> territorioOpt = territorioService.alterar_estado_territorio(codigoTerritorio, nome);
            if (territorioOpt.isPresent()) {
                model.addAttribute("mensagem", "Estado do território alterado com sucesso!");
            } else {
                model.addAttribute("mensagem", "Território não encontrado!");
            }
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
        }
        return atualizarListaTerritorios(model);
    }
    @PostMapping("/alterar-estado-territorio")
    public String alterarEstadoTerritorio(Territorio territorio, Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuariosService.findByEmailUsuario(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String nome = usuario.getUserNome();
            if (territorio.getAtivo()) {
                territorio.setStatus("Inativado");
            } else {
                String previousStatus = territorio.getStatus().replace("Inativado ", "").replace(" ", "");
                territorio.setStatus(previousStatus);
            }
            territorioService.alterar_estado_territorio(territorio, nome);
            model.addAttribute("mensagem", "Estado do território alterado com sucesso!");
        } else {
            model.addAttribute("mensagem", "Usuário não encontrado!");
        }
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

    @GetMapping("/filiais-por-unidade")
    public ResponseEntity<List<String>> getFiliaisPorUnidade(@RequestParam String nomeUnidade) {
        List<Territorio> territorios = territorioService.findByNomeUnidade(nomeUnidade);
        List<String> filiais = territorios.stream()
                .map(Territorio::getCodigoFilial)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/unidade-por-filial")
    public ResponseEntity<String> getUnidadePorFilial(@RequestParam String codigoFilial) {
        Optional<Territorio> territorioOpt = territorioService.findFirstByCodigoFilial(codigoFilial);
        if (territorioOpt.isPresent()) {
            return ResponseEntity.ok(territorioOpt.get().getNomeUnidade());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unidade não encontrada");
        }
    }
}