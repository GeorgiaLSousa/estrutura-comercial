package com.inteligencia.ciclo_comercial.Service;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import com.inteligencia.ciclo_comercial.Repository.TerritorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerritorioService {

    @Autowired
    private TerritorioRepository territorioRepository;

    public Territorio save(Territorio territorio) {
        return territorioRepository.save(territorio);
    }

    public List<Territorio> findAll() {
        return territorioRepository.findAll();
    }

    public List<Territorio> findByNome(String nome) {
        return territorioRepository.findByNome(nome);
    }

    public Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio) {
        return territorioRepository.findByCodigoTerritorio(codigoTerritorio);
    }

    public Optional<Territorio> alterar_estado_territorio(String codigoTerritorio) {
        Optional<Territorio> territorioOpt = territorioRepository.findByCodigoTerritorio(codigoTerritorio);
        if (territorioOpt.isPresent()) {
            Territorio territorio = territorioOpt.get();
            territorio.setAtivo(!territorio.getAtivo());
            territorioRepository.save(territorio);
        }
        return territorioOpt;
    }

    public void alterar_estado_territorio(Territorio territorio) {
        territorio.setAtivo(!territorio.getAtivo());
        territorioRepository.save(territorio);
    }
}