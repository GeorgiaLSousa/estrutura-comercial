package com.inteligencia.ciclo_comercial.Repository;

import com.inteligencia.ciclo_comercial.Model.Territorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.util.List;

@Repository
public interface TerritorioRepository extends JpaRepository<Territorio, Long> {

    @Query("SELECT p FROM Territorio p WHERE LOWER(p.codigoTerritorio) LIKE LOWER(CONCAT('%', :codigoTerritorio, '%'))")
    List<Territorio> findByNome(@Param("codigoTerritorio") String codigoTerritorio);

    Optional<Territorio> findByCodigoTerritorio(String codigoTerritorio);


}