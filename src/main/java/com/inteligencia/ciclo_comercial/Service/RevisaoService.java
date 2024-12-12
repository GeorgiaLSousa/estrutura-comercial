package com.inteligencia.ciclo_comercial.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RevisaoService {

    private static final Logger logger = LoggerFactory.getLogger(RevisaoService.class);

    @Autowired
    private TerritorioRepository territorioRepository;

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

    public void atualizarEmail(String codigoTerritorio, String codigoRegional, String codigoFilial, String email) {
        logger.info("Atualizando email para o território: {}", codigoTerritorio);

        if (codigoTerritorio != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setEmailRTV(email);
                territorioRepository.save(territorio);
                logger.info("Email atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

        } else if (codigoRegional != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findFirstByCodigoRegional(codigoRegional);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setEmailRTV(email);
                territorioRepository.save(territorio);
                logger.info("Email atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

        } else if (codigoFilial != null) {
            Optional<Territorio> territorioOpt = territorioRepository.findFirstByCodigoFilial(codigoFilial);
            if (territorioOpt.isPresent()) {
                Territorio territorio = territorioOpt.get();
                territorio.setEmailRTV(email);
                territorioRepository.save(territorio);
                logger.info("Email atualizado com sucesso para o território: {}", codigoTerritorio);
            } else {
                logger.warn("Território não encontrado: {}", codigoTerritorio);
            }

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
}