package com.inteligencia.ciclo_comercial.Service;

import com.inteligencia.ciclo_comercial.Model.UserAtivo;
import com.inteligencia.ciclo_comercial.Repository.UserAtivoRepository;
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
public class RevisaoService {

    private static final Logger logger = LoggerFactory.getLogger(RevisaoService.class);

    @Autowired
    private TerritorioRepository territorioRepository;

    @Autowired
    private UserAtivoRepository userAtivoRepository;

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

    public Optional<Territorio> findFirstByCodigoRegional(String codigoRegional) {
        List<Territorio> territorios = territorioRepository.findByCodigoRegional(codigoRegional);
        return territorios.stream().findFirst();
    }

    public void atualizarEmail(String codigoTerritorio, String codigoRegional, String codigoFilial, String emailRTV) {
        Optional<Territorio> territorioOpt = findTerritorio(codigoTerritorio, codigoRegional, codigoFilial);

        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setEmailRTV(emailRTV);
            territorioRepository.save(territorio);
            logger.info("Email atualizado com sucesso para o território: {}", codigoTerritorio);
        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }

    public void atualizarModificadoPor(String codigoTerritorio, String codigoRegional, String codigoFilial, String modificadoPor) {
        Optional<Territorio> territorioOpt = findTerritorio(codigoTerritorio, codigoRegional, codigoFilial);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setModificadoPor(modificadoPor);
            territorioRepository.save(territorio);
            logger.info("ModificadoPor atualizado com sucesso para o território: {}", codigoTerritorio);
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
                } else if (currentStatus == null) {
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

    private Optional<Territorio> findTerritorio(String codigoTerritorio, String codigoRegional, String codigoFilial) {
        if (codigoTerritorio != null) {
            return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        } else if (codigoRegional != null) {
            return territorioRepository.findFirstByCodigoRegional(codigoRegional);
        } else if (codigoFilial != null) {
            return territorioRepository.findFirstByCodigoFilial(codigoFilial);
        }
        return Optional.empty();
    }

    public boolean validarEmailEAtivo(String email) {
        String trimmedEmail = email.trim().toLowerCase();
        logger.info("Procurando usuário com email: {}", trimmedEmail);
        Optional<UserAtivo> userAtivoOpt = userAtivoRepository.findByEmail(trimmedEmail);
        if (userAtivoOpt.isPresent()) {
            UserAtivo userAtivo = userAtivoOpt.get();
            String status = userAtivo.getStatus();
            logger.info("Status para o email {}: {}", trimmedEmail, status);
            if (status == null) {
                logger.warn("Status é nulo para o email: {}", trimmedEmail);
                return false;
            }
            boolean isActive = "ACTIVE".equalsIgnoreCase(status.trim());
            logger.info("Usuário está ativo: {}", isActive);
            return isActive;
        }
        logger.warn("Usuário com email {} não encontrado", trimmedEmail);
        return false;
    }
}