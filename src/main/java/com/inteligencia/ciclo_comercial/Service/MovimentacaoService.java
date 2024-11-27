package com.inteligencia.ciclo_comercial.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inteligencia.ciclo_comercial.Model.Territorio;
/*import com.inteligencia.ciclo_comercial.Model.Usuarios;*/
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
//import com.inteligencia.ciclo_comercial.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoService {

    private static final Logger logger = LoggerFactory.getLogger(RevisaoService.class);

    @Autowired
    private TerritorioRepository territorioRepository;

//    @Autowired
//    private UsuariosRepository usuariosRepository;

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

//    public List<Usuarios> findAllUsuarios() {
//        return usuariosRepository.findAll();
//    }

    public void atualizarRegional(String codigoTerritorio, String codigoRegional) {
        logger.info("Atualizando email para o território: {}", codigoTerritorio);
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setCodigoRegional(codigoRegional);
            territorioRepository.save(territorio);
            logger.info("Codigo Regional atualizado com sucesso para o território: {}", codigoTerritorio);
        } else {
            logger.warn("Território não encontrado: {}", codigoTerritorio);
        }
    }
}