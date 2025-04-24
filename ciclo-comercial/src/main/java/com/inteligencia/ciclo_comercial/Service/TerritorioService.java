package com.inteligencia.ciclo_comercial.Service;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TerritorioService {

    private static final Logger logger = LoggerFactory.getLogger(TerritorioService.class);

    @Autowired
    private TerritorioRepository territorioRepository;

    public Territorio save(Territorio territorio, String username) {
        if (territorio.getStatus() == null || territorio.getStatus().isEmpty()) {
            territorio.setStatus("Criado");
        }
        territorio.setModificadoPor(username);

        // Set the modification time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().minusHours(3).format(formatter);
        territorio.setDataModificacao(currentTime);

        return territorioRepository.save(territorio);
    }

    public List<Territorio> findAll() {
        List<Territorio> territorios = territorioRepository.findAll();
        territorios.sort(Comparator.comparing(Territorio::getNomeUnidade)
                .thenComparing(Territorio::getCodigoFilial));
        return territorios;
    }

    public List<Territorio> findByNome(String nome) {
        return territorioRepository.findByNome(nome);
    }

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

    public List<Territorio> findByCodigoFilial(String codigoFilial) {
        return territorioRepository.findByCodigoFilial(codigoFilial);
    }

    public List<Territorio> findByNomeUnidade(String nomeUnidade) {
        return territorioRepository.findByNomeUnidade(nomeUnidade);
    }

    public Optional<Territorio> alterar_estado_territorio(String codigoTerritorio, String username) {
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setAtivo(!territorio.getAtivo());
            territorio.setModificadoPor(username);
            if (!territorio.getAtivo()) {
                atualizarStatus(codigoTerritorio, "Inativado");
            } else {
                atualizarStatus(codigoTerritorio, "Ativado");
            }
            territorioRepository.save(territorio);
        }
        return territorioOpt;
    }

    public void alterar_estado_territorio(Territorio territorio, String username) {
        territorio.setAtivo(!territorio.getAtivo());
        territorio.setModificadoPor(username);
        if (!territorio.getAtivo()) {
            atualizarStatus(territorio.getCodigoTerritorio(), "Inativado");
        } else {
            atualizarStatus(territorio.getCodigoTerritorio(), "Ativado");
        }
        territorioRepository.save(territorio);
    }

    public void atualizarStatus(String codigoTerritorio, String status) {
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            String currentStatus = territorio.getStatus();

            if ("Inativado".equals(status)) {
                if (currentStatus == null || !currentStatus.contains("Inativado")) {
                    territorio.setStatus("Inativado" + (currentStatus != null ? " (" + currentStatus + ")" : ""));
                }
            } else if ("Ativado".equals(status)) {
                if (currentStatus != null && currentStatus.startsWith("Inativado (")) {
                    String previousStatus = currentStatus.substring(10, currentStatus.length() - 1);
                    territorio.setStatus(previousStatus.isEmpty() ? null : previousStatus);
                } else {
                    territorio.setStatus(null);
                }
            } else {
                if (currentStatus != null && !currentStatus.contains(status)) {
                    territorio.setStatus(currentStatus + " e " + status);
                } else {
                    territorio.setStatus(status);
                }
            }

            // Clean up any extra parentheses and handle null status
            String cleanedStatus = territorio.getStatus() != null ? territorio.getStatus()
                    .replaceAll("\\(\\(", "(")
                    .replaceAll("\\)\\)", ")")
                    .replaceAll("Inativado Inativado", "Inativado")
                    .replaceAll("\\(null\\)", "")
                    .replaceAll("\\(\\)", "")
                    .replaceAll("null", "")
                    .replaceAll("^\\(", "") // Remove leading parenthesis
                    : null;
            territorio.setStatus(cleanedStatus);

            // Set the modification time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTime = LocalDateTime.now().minusHours(3).format(formatter);
            territorio.setDataModificacao(currentTime);

            territorioRepository.save(territorio);
            logger.info("Status atualizado com sucesso para o território: {}", codigoTerritorio);
        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }

    public Optional<Territorio> findFirstByCodigoRegional(String codigoRegional) {
        List<Territorio> territorios = territorioRepository.findByCodigoRegional(codigoRegional);
        return territorios.stream().findFirst();
    }

    public Optional<Territorio> findFirstByCodigoFilial(String codigoFilial) {
        List<Territorio> territorios = territorioRepository.findByCodigoFilial(codigoFilial);
        return territorios.stream().findFirst();
    }

    public Optional<Territorio> findFirstByNomeUnidade(String nomeUnidade) {
        List<Territorio> territorios = territorioRepository.findByNomeUnidade(nomeUnidade);
        return territorios.stream().findFirst();
    }

    public boolean existsByNomeUnidade(String nomeUnidade) {
        return territorioRepository.existsByNomeUnidade(nomeUnidade);
    }
}