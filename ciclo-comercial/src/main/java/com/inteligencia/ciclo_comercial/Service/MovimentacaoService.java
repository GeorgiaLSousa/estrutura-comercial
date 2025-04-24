package com.inteligencia.ciclo_comercial.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimentacaoService.class);

    @Autowired
    private TerritorioRepository territorioRepository;

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

    public Optional<Territorio> findFirstByCodigoRegional(String codigoRegional) {
        List<Territorio> territorios = territorioRepository.findByCodigoRegional(codigoRegional);
        return territorios.stream().findFirst();
    }

    public void atualizarRegional(String codigoTerritorio, String codigoRegional, String codigoFilial, String nomeUnidade) {
        logger.info("Atualizando dados para o território: {}", codigoTerritorio);
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setCodigoRegional(codigoRegional);
            territorio.setCodigoFilial(codigoFilial);
            territorio.setNomeUnidade(nomeUnidade);
            territorioRepository.save(territorio);
            logger.info("Dados atualizados com sucesso para o território: {}", codigoTerritorio);
        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }

    public void atualizarModificadoPor(String codigoTerritorio, String codigoRegional, String codigoFilial, String modificadoPor) {
        logger.info("Atualizando modificadoPor para o território: {}", codigoTerritorio);

        if (codigoTerritorio != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setModificadoPor(modificadoPor);
                territorioRepository.save(territorio);
                logger.info("modificadoPor atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

        } else if (codigoRegional != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findFirstByCodigoRegional(codigoRegional);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setModificadoPor(modificadoPor);
                territorioRepository.save(territorio);
                logger.info("modificadoPor atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

        } else if (codigoFilial != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findFirstByCodigoFilial(codigoFilial);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setModificadoPor(modificadoPor);
                territorioRepository.save(territorio);
                logger.info("modificadoPor atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }

    public void atualizarStatus(String codigoTerritorio, String status) {
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            String currentStatus = territorio.getStatus();

            if ("Inativado".equals(status)) {
                if (!"Inativado".equals(currentStatus)) {
                    territorio.setStatus("Inativado (" + currentStatus + ")");
                }
            } else if ("Ativado".equals(status)) {
                if (currentStatus != null && currentStatus.startsWith("Inativado (")) {
                    String previousStatus = currentStatus.substring(10, currentStatus.length() - 1);
                    territorio.setStatus(previousStatus);
                }
            } else {
                if (currentStatus != null && !currentStatus.contains(status)) {
                    territorio.setStatus(currentStatus + " e " + status);
                } else {
                    territorio.setStatus(status);
                }
            }

            // Set the modification time to 3 hours earlier
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTime = LocalDateTime.now().minusHours(3).format(formatter);
            territorio.setDataModificacao(currentTime);

            territorioRepository.save(territorio);
            logger.info("Status atualizado com sucesso para o território: {}", codigoTerritorio);
        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }
}